/**
 * Instancias desta classe representam vias rapidas onde veiculos que chegam a
 * porticos aguardam a sua vez para o pagamento de respetivas portagens,
 * avancando uma vez saldada a sua divida.
 * 
 * A cada momento podemos determinar o tempo decorrido, tempo de espera dos
 * veiculos, e numero de veiculos processados.
 * 
 * @author Madalena Machado fc59858
 *
 */
public class Highway {

	// Numero minimo de filas (porticos de portagens) que devem estar ativas
	private int minActivatedQueues;
	// Numero maximo de veiculos por fila
	private int maxVehiclesPerQueue;
	// Sequencia de filas
	private QueueSystem<Vehicle> q;
	// Unidades de tempo decorridas
	private int elapsedTime;
	// Tempo total de espera
	private int totalWaitTime;
	// Numero de veiculos cujo pagamento ja foi processado
	private int numVehiclesProcessed;
	// Valor total de portagens cobradas
	private double tollsCollected;

	/**
	 * Construtor
	 * 
	 * @param minActivatedQueues  Numero minimo de filas que deve estar ativas
	 * @param maxVehiclesPerQueue Numero maximo de veiculos por fila
	 * 
	 * @requires minActivatedQueues > 0 && maxVehiclesPerQueue > 0
	 */
	public Highway(int minActivatedQueues, int maxVehiclesPerQueue) {

		this.minActivatedQueues = minActivatedQueues;
		this.maxVehiclesPerQueue = maxVehiclesPerQueue;
		elapsedTime = 0;
		totalWaitTime = 0;
		numVehiclesProcessed = 0;
		q = new ArrayQueueSystem<>(minActivatedQueues);
		tollsCollected = 0.0;
	}

	/**
	 * Numero total de veiculos nas filas desta via rapida (inclui aqueles cujo
	 * processamento ainda se encontra a decorrer)
	 */
	public int totalNrVehicles() {
		return q.size();
	}

	/**
	 * Numero de filas ativas nesta via rapida
	 */
	public int nrActiveQueues() {
		return q.howManyActiveQueues();
	}

	/**
	 * Numero medio de veiculos por fila ativa neste instante
	 */
	public double averageVehiclesPerQueue() {
		return (double) totalNrVehicles() / nrActiveQueues();
	}

	/**
	 * Tempo medio de espera de veiculos na fila
	 */
	public double averageWaitingTime() {
		return (double) this.totalWaitTime / numVehiclesProcessed;
	}

	/**
	 * Valor total de portagens cobradas
	 */
	public double totalTolls() {
		return this.tollsCollected;
	}

	/**
	 * Valor medio de portagens cobradas por veiculo processado
	 */
	public double averageTolls() {
		return totalTolls() / this.numVehiclesProcessed;
	}

	/**
	 * Adiciona um veiculo a uma fila ativa: aquela com o menor numero de veiculos
	 * em espera. Se todas as filas estiverem na sua capacidade maxima, se existir
	 * uma fila inativa esta torna-se ativa, caso contrario e criada uma nova fila.
	 * 
	 * @param v O veiculo que se quer juntar a uma fila
	 * @requires v != null
	 * @throws IllegalQueueRequest
	 */
	public void addVehicle(Vehicle v) throws IllegalQueueRequest {
		boolean isFull = q.focusMin() == this.maxVehiclesPerQueue;
		int index = 0;
		boolean activates = true;

		if (!isFull) {    //fila com menos veiculos em espera tem lugares
			q.focusMin();
			q.enqueue(v); 
		} else if (q.howManyQueues() > q.howManyActiveQueues()) { //fila com menos veiculos em espera esta cheia mas há inativas
			while (index < q.howManyQueues() && activates) {
				if (!q.isActivated(index)) {
					q.activate(index);
					q.focus(index);
					q.enqueue(v);
					activates = false;
				}
				index++;
			}
		} else { //não há inativas entao cria-se uma nova
			q.create();
			q.activate(q.howManyQueues() - 1);
			q.focus(q.howManyQueues() - 1);
			q.enqueue(v);
		}

	}

	/**
	 * Processa uma unidade de tempo em todas as filas ativas: - atualiza as
	 * unidades de tempo decorridas; - atualiza o estado das filas; - decrementa uma
	 * unidade de tempo ao tempo de espera de cada veiculo que esta a ser processado
	 * (aqueles na primeira posicao numa fila); - se o tempo de espera de um veiculo
	 * ficar a zero, ele abandona a fila e atualizam-se os valores de totalWaitTime,
	 * numVehiclesProcessed
	 * 
	 * @throws IllegalQueueRequest
	 */
	public void updateActiveQueues() throws IllegalQueueRequest {
		int queue = 0;
		this.elapsedTime++;

		while (queue < q.howManyQueues()) {
			if (q.isActivated(queue)) {
				q.focus(queue);
				if (!q.isEmpty()) {
					q.front().decreaseOneTimeUnit();
					if (q.front().timeLeft() == 0) { // tempo de espera acabar
						this.tollsCollected += q.front().toll();
						this.totalWaitTime += this.elapsedTime - q.front().duration() - q.front().arrival() + 1;
						q.dequeue();
						this.numVehiclesProcessed++;
					}
				}
			}
			queue++;
		}
	}

	/**
	 * Havendo mais do que uma fila vazia, desativa a fila vazia de menor indice,
	 * conquanto o numero minimo de filas ativas seja respeitado
	 * 
	 * @throws IllegalQueueRequest
	 */
	public void updateNumberActiveQueues() throws IllegalQueueRequest {
		int index = 0;
		int low = 0; // guarda o indice da fila vazia de menor indice
		int nrEmptyQueues = 0;
		while (index < q.howManyQueues() && nrEmptyQueues <= 1) {
			if (q.isActivated(index)) {
				q.focus(index);
				if (q.isEmpty()) {
					nrEmptyQueues++;
					if (nrEmptyQueues == 1) {
						low = index;
					}
				}
			}
			index++;
		}
		if (nrEmptyQueues > 1 && q.howManyActiveQueues() > this.minActivatedQueues) { // considerando a fila a ser
																						// retirada
			q.deactivate(low);
		}
	}

	/**
	 * Representacao textual da via rapida
	 */
	public String toString() {
		String END_LINE = System.lineSeparator();
		StringBuilder sb = new StringBuilder();
		sb.append("Minimum number of activated queues " + minActivatedQueues + END_LINE);
		sb.append("Maximum number of vehicles per queue " + maxVehiclesPerQueue + END_LINE);
		sb.append(q.toString());
		sb.append("Elapsed time " + elapsedTime + END_LINE);
		sb.append("Total waiting time " + totalWaitTime + END_LINE);
		sb.append("Number of vehicles processed " + numVehiclesProcessed + END_LINE);
		sb.append(String.format("Total tolls collected %.2f", tollsCollected) + END_LINE);
		return sb.toString();
	}

	/**
	 * Imprime uma mensagem no standard output; usar apenas durante o
	 * desenvolvimento de codigo
	 * 
	 * @param msg a mensagem a imprimir
	 */
	private void log(String msg) {
		System.out.println(msg);
	}
}
