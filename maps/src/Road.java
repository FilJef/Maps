

/**
 * Represents a settlement with a name, population, type and
 * with connected roads.
 * @author Chris Loftus
 * @version 1.0 (30th January 2015)
 */
public class Road {
	private String name;
	private Classification classification;
	private Settlement sourceSettlement;
	private Settlement destinationSettlement;
	private double length;
	
	/**
	 * Constructor to build road between two settlements. This fulfills the class diagram
	 * constraint that every road must be connected to two settlements. We are not
	 * checking whether it's the same settlement, but that's okay: you drive out and arrive
	 * back again at the same place!
	 * @param nm The road name
	 * @param classifier The class of road, e.g. 'A'
	 * @param source The source settlement
	 * @param destination The destination settlement (can be the same as the source!)
	 */
	public Road(String nm, Classification classifier, Settlement source, Settlement destination, double len){
		name = nm;
		classification = classifier;
		sourceSettlement = source;
		destinationSettlement = destination;
		length = len;
	}
	
	/**
	 * The name of the road
	 * @return The road's name
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String nm){
		name = nm;
	}
	
	public void setLength(double len) {
		length = len;
	}
	
	public double getLength(){
		return length;
	}
	
	/**
	 * The road's class
	 * @return The class of the road, e.g. A
	 */
	public Classification getClassification() {
		return classification;
	}
	
	/**
	 * The source settlement
	 * @return One end of the road we call source
	 */
	public Settlement getSourceSettlement() {
		return sourceSettlement;
	}
	
	/**
	 * The destination settlement
	 * @return One end of the road we call destination
	 */
	public Settlement getDestinationSettlement() {
		return destinationSettlement;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Road other = (Road) obj;
		if (destinationSettlement == null) {
			if (other.destinationSettlement != null)
				return false;
		} else if (!destinationSettlement.equals(other.destinationSettlement))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sourceSettlement == null) {
			if (other.sourceSettlement != null)
				return false;
		} else if (!sourceSettlement.equals(other.sourceSettlement))
			return false;
		return true;
	}


	/**
	 * Some useful info about the state of this object. Notice how this also
	 * returns the names of the connected settlements
	 * @param  
	 * @return The state of the object
	 */
	public String toString() {
		String result = "Road " + name + " with classifaction " + classification + ", from " + sourceSettlement.getName() + ", to " + destinationSettlement.getName();
		return result;
	}
}
