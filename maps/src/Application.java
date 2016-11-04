
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * Main class to test the Road and Settlement classes
 * 
 * @author Phillip Jefferies (pmj5)
 * @version 1.1 (4th march 2016)
 *
 */
public class Application {

	private Scanner scan;
	private Map map;
	private Settlement temp;
	
	public Application() {
		scan = new Scanner(System.in);
		map = new Map();
	}
	/**
	 * prints the menu and gets the choice
	 * @throws IOException
	 */
	private void runMenu() throws IOException {
		String choice;
		do{
			printMenu();
			choice = scan.nextLine().toUpperCase();
			switch(choice){
			case "1":
				createSettlement();
				break;
			case "2":
				createRoad();
				break;
			case "3":
				System.out.print(map.toString());
				break;
			case "4":
				deleteSettlement();
				break;
			case "5":
				map.removeRoads();
				System.out.print("Road removed");
				break;
			case "6":
				save();
				break;
			case "7":
				load();
				break;
			case "8":
				map.edit();
				break;
			case "Q":
				System.out.println("Thanks for using my map program!");
		}	
		}while(!(choice.equals("Q")));

	}

	// STEP 1: ADD PRIVATE UTILITY MENTHODS HERE. askForRoadClassifier, save and load provided
	
	/**
	 * gets the first load of data and checks inputs before allowing the user to create the settlement
	 */
	private void createSettlement(){
		String name;
		SettlementType kind;
		System.out.print("Please enter the name of the settlement");
		name = scan.nextLine();
		while(map.checkname(name) == false){
			System.out.print("Please enter a new name:");
			name = scan.nextLine();
		}
		System.out.println("New settlement called " + name);
		kind = map.askForSettlementClassifier();
		temp = new Settlement(name,kind);
		System.out.print(temp.toString());
		map.addSettlement(temp);
	}
	

	/**
	 * checks that the user is looking for a settlement that exists and has a backup option
	 */
	private void deleteSettlement(){
		boolean exists = false;
		do{
			System.out.println("Please enter the name of the settlement you want to remove, or Q to quit: ");
			String nm = scan.nextLine();
			nm = nm.toUpperCase();
			if(nm.equals("Q")){
				exists = true;
			}
			else{
				exists = map.exists(nm);
			}
		}while(!exists);
	}
	
	/**
	 * allows the user to create a road
	 */
	private void createRoad(){
		map.addRoad();
	}
	
	/**
	 * This is the road classifier sub that has been re written to handle settlement type
	 * @return
	 */

	
	private void save() throws FileNotFoundException, UnsupportedEncodingException {
		String setName, rdName;
		boolean check = true;
		do{
			System.out.print("What file do you want to save the settlements to?");
			setName = scan.nextLine();
			if(setName == ""){
				check = false;
			}
		}while(!check);
		
		check = true;
		do{
			System.out.print("What file do you want to save the roads to?");
			rdName = scan.nextLine();
			if(rdName == ""){
				check = false;
			}
		}while(!check);
		
		map.save(setName, rdName);
	}

	private void load() throws IOException {
		String setName, rdName;

		System.out.print("What file do you want to load the settlements from (default, Settlements): ");
		setName = scan.nextLine();

		System.out.print("What file do you want to load the roads from(default, Roads): ");
		rdName = scan.nextLine();

		map.load(setName,rdName);
	}

	private void printMenu() {
		System.out.print("\n Enter: \n"
				+ " 1 - Create Settlement \n"
				+ " 2 - Create Road \n"
				+ " 3 - Display Map \n"
				+ " 4 - Delete Settlement \n"
				+ " 5 - Delete Road \n"
				+ " 6 - Save \n"
				+ " 7 - Load \n"
				+ " 8 - Edit Settlements \n"
				+ " Q - Quit \n"
				+ " Input: ");
	}

	public static void main(String args[]) throws IOException {
		Application app = new Application();
		app.load();
		app.runMenu();
		app.save();
	}

}
