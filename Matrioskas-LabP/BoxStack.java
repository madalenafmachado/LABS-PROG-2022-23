package project;

import backbone.ArrayStack;
import backbone.Stack;
import backbone.Utils;
/**
 * 
 * @author fc59858 Madalena Machado
 *
 */
public class BoxStack implements Stack<Box>{

    private Stack<Box> boxStack;
    private int weight;
    private int height;
    
    /**
     * Verifies if the lightest box on the first boxstack is heavier than the heaviest box
     * in the second boxstack given
     * 
     * @param first    first boxstack to be compared with
     * @param second   second boxstack
     * @return 
     * @requires {@code first != null && second != null}
     */
    public static boolean canPile(BoxStack first, BoxStack second){
    	return first.peek().getWeight() > Utils.invertStack(second).peek().getWeight();
    }
    
    /**
     * Constructs a new BoxStack
     */
    public BoxStack() {
    	boxStack = new ArrayStack<Box>();
    	this.height = 0;
    	this.weight = 0;
    }

    /**
     * Returns the current height of the boxstack
     * 
     * @return boxstack height
     */
    public int getHeight() {
    	return this.height;
    }
    
    /**
     * Returns the current weight of the boxstack
     * 
     * @return boxstack weight
     */
    public int getWeight() {
    	return this.weight;
    }
    
    /**
     * Piles other boxstack on the current boxstack
     * 
     * @param other given boxstack
     * @requires {@code canPile(boxStack, other)}
     */
    public void pile(BoxStack other){
    	Stack <Box> copy = Utils.invertStack(other.boxStack);
    	
    	while(!copy.isEmpty()) {
    		this.boxStack.push(copy.peek());
    		this.weight += copy.peek().getWeight();
    		this.height ++;
    		
        	copy.pop();
    	}
    }

    @Override
    public boolean isEmpty() {
    	return this.weight == 0;
    }

    @Override
    public BoxStack copy() {
    	BoxStack copy = new BoxStack();
		copy.weight = this.weight;
		copy.height = this.height;		
		copy.boxStack = this.boxStack.copy();
		return copy;
    }

    /**
     * 
     * 
     * @requires Box.heavierThan(this.peek(),box)
     */
    @Override
    public void push(Box box) {
    	boxStack.push(box);
    	this.weight += box.getWeight();
    	this.height++;
    }

    @Override
    public void pop() {
    	this.weight -= boxStack.peek().getWeight();
    	this.height--;
    	boxStack.pop();
    }
    
    @Override
    public Box peek() {
    	return boxStack.peek().copy();
    }
    
    /**
     * Retorna uma representação de string do estado atual de uma instancia BoxStack.

       A string inclui o peso de todo o BoxStack e uma representação de string de cada Box
       dentro da pilha, listados na ordem inversa a que foram adicionados à pilha.
       @return uma representação textual do BoxStack
     */
    @Override
    public String toString() {
        BoxStack toPrint = this.copy();
        StringBuilder sb = new StringBuilder("↙----Box Stack----↘");
        sb.append(" w: ").append(getWeight());
        sb.append(System.lineSeparator());
        while(!toPrint.isEmpty())
            sb.append(Utils.peekPop(toPrint).toString());
        sb.append("↖-------End-------↗");
        sb.append(System.lineSeparator());
        return sb.toString();
    }
    
    /*
     * 
     * Método desafio. Implementacao facultativa
     * 
     */
    public void pack(){}
    
    

}