import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * 
 * @author Chris Loftus 
 * @version 1.0 (25th February 2016)
 *
 */

public class Map {

	private ArrayList<Settlement> Settlements = new ArrayList<Settlement>();
	private Scanner input = new Scanner(System.in);
	private Settlement Source, Destination;
	private int location1, location2;
	
	public Map() {
		// INSERT CODE
	}

	/**
	 * In this version we display the result of calling toString on the command
	 * line. Future versions may display graphically
	 */
	public void display() {
		System.out.println(toString());
	}
	
	/**
	 * checks that a settlement name doesnt already exist. also stores the location in the array if it does
	 * @param nm
	 * @return
	 */
	public boolean checkname(String nm){
		if(0 < Settlements.size()){
			for(int i = 0;i < Settlements.size(); i++){
				Settlement temp;
				temp = Settlements.get(i);
				String townName = temp.getName();
				nm = nm.toUpperCase();
				townName = townName.toUpperCase();
				if(nm.equals(townName)){
					location1 = i;
					System.out.println("This town exists");
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * used for deletion, similar to the above but also deletes
	 * @param nm
	 * @return
	 */
	public boolean exists(String nm){
		if(0 < Settlements.size()){
			for(Settlement s: Settlements){
				String townName = s.getName();
				townName = townName.toUpperCase();
				if(nm.equals(townName)){
					System.out.println("Town removed");
					deleteRoads(s.getAllRoads(), s);
					Settlements.remove(s);
					return true;
				}
			}
		}
		System.out.println("no settlement with that name exists!");
		return false;
	}
	
	/**
	 * continues the creation of a road, this gets the source, destination and length.
	 * then it creates the road and attaches it to an object
	 * @param nm
	 * @param classifier
	 */
	public void addRoad(){
		String name;
		String source;
		String destination;
		double length = 0;
		boolean finish = false;
		
		do{
			finish = true;
			System.out.print("Where does the road come from: ");
			source = input.nextLine();
			finish = checkname(source);
		}while(finish);
		
		location2 = location1;
		Source = Settlements.get(location2);
		
		do{
			finish = true;
			System.out.print("Where does the road go: ");
			destination = input.nextLine();
			finish = checkname(destination);
		}while(finish);
		Destination = Settlements.get(location1);
		
		finish = true;
		do{
			System.out.print("What is the road called: ");
			name = input.nextLine().toUpperCase();
			finish = checkRoadDNE(name, Source, Destination);
		}while(finish);

		Classification classifier = askForRoadClassifier();
		
		do{
			try{ //makes sure the users inputs an int
				finish = true;
				System.out.print("How long is the road in miles: ");
				length = Double.parseDouble(input.nextLine());
			}
			catch(Exception e){
				System.out.print("Not a valid input!");
				finish = false;
			}
		}while(!finish);
		
		Road temp = new Road(name, classifier, Source, Destination, length);
		System.out.print("New road: " + temp.toString());
		Source.add(temp);
		Destination.add(temp);
		
		Settlements.set(location1, Destination);
		Settlements.set(location2, Source);
	}
	
	public void edit(){
		boolean done = true;
		int pos;
		Settlement temp = null;
		do{
			System.out.println("Please enter which settlement number you wish to edit");
			for(int i=0;i < Settlements.size();i++){
				System.out.println((i+1) + ": " + Settlements.get(i).getName());
			}
			String choice = input.nextLine();
			try{
				done = true;
				temp = Settlements.get((Integer.parseInt(choice)-1));
				pos = Settlements.indexOf(temp);
			}
			catch(Exception e){
				System.out.print("Not a valid input!");
				done = false;
			}
		}while(!done);
		
		displaySettlement(temp);
	}
	
	public void displaySettlement(Settlement edit){
		int size = 0;
		String choice;
		do{
			System.out.print("Settlement info: \n Name: " + edit.getName() + "\n Type: " + edit.getKind() + "\n Population: " + edit.getPopulation() + " \n Roads attached: " + edit.listRoads() + 
				"\n 1 - Edit Type \n 2 - Edit Population \n Q - quit \n Input: ");
			choice = input.nextLine().toUpperCase();
			if(choice.equals("1")){
				edit.setKind(askForSettlementClassifier());
			}
			else if(choice.equals("2")){
				boolean finish = false;
				do{
					try{ //makes sure the users inputs an int
						finish = true;
						System.out.print("What is the new population: ");
						size = Integer.parseInt(input.nextLine());
					}
					catch(Exception e){
						System.out.print("Not a valid input!");
						finish = false;
					}
				}while(!finish);
				edit.setPopulation(size);
			}
			else if(choice.equals("Q")){
				System.out.print("returning to main menu");
			}
			else{
				System.out.print("Not a valid input!");
			}
		}while(!(choice.equals("Q")));
	}
	
	private boolean checkRoadDNE(String name, Settlement source, Settlement dest){
		ArrayList<Road> temp = new ArrayList<Road>();
		temp = source.findRoads(name);
		for(Road r: temp){
			if((r.getDestinationSettlement() == dest && r.getSourceSettlement() == source)||(r.getDestinationSettlement() == source && r.getSourceSettlement() == dest)){
				System.out.print("This road already exists!");
				return true;
			}
		}
		return false;
	}
	
	/**
	 * adds a settlement
	 * @param newSettlement
	 * @throws IllegalArgumentException
	 */
	public void addSettlement(Settlement newSettlement) throws IllegalArgumentException {
		Settlements.add(newSettlement);
	}

	/**
	 * Handles deleting all the roads from a settlement and detaching it from the settlements
	 * @param rd
	 * @param condemned
	 */
	public void deleteRoads(ArrayList<Road> rd, Settlement condemned){
		Settlement temp;
		ArrayList<Road> temporary = new ArrayList<Road>(); 
		for(Road r: rd){
			temp = r.getSourceSettlement();
			temporary = temp.getAllRoads();
			for(Road t: temporary){
				if(t.getDestinationSettlement().equals(condemned)|| t.getSourceSettlement().equals(condemned)){
					temp.delete(t);
					replace(temp);
					
				}
			}
			temp = r.getDestinationSettlement();
			temporary = temp.getAllRoads();
			for(Road t: temporary){
				if(t.getDestinationSettlement().equals(condemned)|| t.getSourceSettlement().equals(condemned)){
					temp.delete(t);
					replace(temp);
				}
			}
		}
	}
	
	/**
	 * deletes a single road
	 * @param rd
	 */
	public void deleteRoad(Road rd){
		Settlement temp;
		temp = rd.getSourceSettlement();
		temp.delete(rd);
		replace(temp);
		temp = rd.getDestinationSettlement();
		temp.delete(rd);
		replace(temp);
	}
	
	/**
	 * finds a settlement and replaces it with a new version(used for attaching or detaching roads)
	 * @param set
	 */
	public void replace(Settlement set){
		int i = Settlements.indexOf(set);
		Settlements.set(i, set);
	}
	
	/**
	 * checks that a road doesnt already exist
	 * @return
	 */
	public Road getRoadName(){
		System.out.print("What is the road called");
		String name = input.nextLine().toUpperCase();
		ArrayList<Road> rd = new ArrayList<Road>();
		for(Settlement g: Settlements){
			rd = g.getAllRoads();
			for(Road r: rd){
				if(r.getName().equals(name)){
					return r;
				}
			}
		}
		return null;
	}
	
	/**
	 * reads from a text file
	 * @throws IOException
	 */
	public void load(String setName,String rdName) throws IOException {
		boolean finish = false;
		do{
			try{
				finish = true;
				List<String> lines = Files.readAllLines(Paths.get(setName + ".txt"));
				lines.forEach(this::add);
			}
			catch(Exception e){
				finish = false;
				System.out.print("Settlement file is either corrupted, doesnt exist or is unreadable, please input another file: ");
				setName = input.nextLine();
			}
		}while(!finish);
		
		finish = true;
		
		do{
			try{
				finish = true;
				List<String> roads = Files.readAllLines(Paths.get(rdName + ".txt"));
				roads.forEach(this::loadRoad);
			}
			catch(Exception e){
				finish = false;
				System.out.print("Road file is either corrupted, doesnt exist or is unreadable, please input another file: ");
				rdName = input.nextLine();
			}
		}while(!finish);
	}
	
	/**
	 * loads roads from a text file
	 * @param rd
	 */
	private void loadRoad(String rd){
		String temp[]; //0=name, 1=type, 2=source, 3=destination, 4=length
		temp = rd.split(":");
		Settlement src = null, dest = null;
		
		for(Settlement s: Settlements){
			if(s.getName().equals(temp[3])){
				src = s;
			}
			if(s.getName().equals(temp[4])){
				dest = s;
			}
		}
		
		Road tempRoad = new Road(temp[0], Classification.valueOf(temp[1]), src, dest, Double.parseDouble(temp[2]));
		src.add(tempRoad);
		replace(src);
		dest.add(tempRoad);
		replace(dest);
	}
	
	/**
	 * adds settlements to a text file
	 * @param stream
	 */
	private void add(String stream){
		if(stream.length() > 5){
			String settlement[];
			settlement = stream.split(":");
			Settlement temp = new Settlement(settlement[0],SettlementType.valueOf(settlement[1]),Integer.parseInt(settlement[2]));
			Settlements.add(temp);
		}
	}
	
	/**
	 * saves data to a text file
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void save(String settlementsFile, String roadsFile) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(settlementsFile + ".txt");
		ArrayList<Road> temproad = new ArrayList<Road>();
		ArrayList<Road> roadsToSave = new ArrayList<Road>();
		boolean found = false;
		
		for(Settlement s: Settlements){
			int temp = s.getPopulation();
			writer.println(String.join(":", s.getName(),s.getKind().toString(),Integer.toString(temp)));
			
			temproad = s.getAllRoads();
			for(Road r: temproad){
				found = false;
				for(Road rd: roadsToSave){
					if(rd.equals(r)){
						found = true;
					}
				}
				if(!found){
					roadsToSave.add(r);
				}
			}
		}
		writer.close();
		
		PrintWriter roadwriter = new PrintWriter(roadsFile + ".txt");
		for(Road r: roadsToSave){
			roadwriter.println(String.join(":", r.getName(),r.getClassification().toString(),Double.toString(r.getLength()),r.getSourceSettlement().getName(),r.getDestinationSettlement().getName()));
		}
		roadwriter.close();
	}

	public String toString() {
		String result = "";
		for(Settlement i: Settlements){
			result += i.toString();
		}
		return result;
	}
	
	/**
	 * removes roads with the same name
	 * @param name
	 */
	public void removeRoads() {
		String name;
		Settlement temp = null;
		do{
			System.out.print("Enter a settlement name: ");
			name = input.nextLine();
		}while((checkname(name)));
		for(Settlement s: Settlements){
			if(s.getName().equals(name)){
				temp =s;
			}
		}
		
		do{
			System.out.print("What is the road called: ");;
			name = input.nextLine().toUpperCase();
		}while(temp.findRoads(name) == null);
		
		ArrayList<Road> tempRoads = new ArrayList<Road>();
		tempRoads = temp.findRoads(name);
		for(Road r: tempRoads){
			deleteRoad(r);
		}
	}
	
	/**
	 * code by (presumabley) chris loftus, it checks a string is a classifier for a road
	 */
	private Classification askForRoadClassifier() {
		Classification result = null;
		boolean valid;
		do{
			valid = false;
			System.out.print("Enter a road classification: ");
			for (Classification cls: Classification.values()){
				System.out.print(cls + " ");
			}
			String choice = input.nextLine().toUpperCase();
			try {
				result = Classification.valueOf(choice);
				valid = true;
			} catch (IllegalArgumentException iae){
				System.out.println(choice + " is not one of the options. Try again.");
			}
		}while(!valid);
		return result;
	}
	
	public SettlementType askForSettlementClassifier(){
		SettlementType result = null;
		boolean valid;
		do{
			valid = false;
			System.out.println("Enter a settlement classification: ");
			for (SettlementType cls: SettlementType.values()){
				System.out.print(cls + " ");
			}
			String choice = input.nextLine().toUpperCase();
			try {
				result = SettlementType.valueOf(choice);
				valid = true;
			} catch (IllegalArgumentException iae){
				System.out.println(choice + " is not one of the options. Try again.");
			}
		}while(!valid);
		return result;
	}

}
