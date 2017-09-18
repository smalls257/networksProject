/**
 * 
 */
package networksProject1;

import java.util.Scanner;

/**
 * @author Jason Smith
 *
 */
public class Program {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		//Console input
		System.out.println("Enter the number corresponding with your selected"+
				" option.");
		
		System.out.println("1.	Host current Date and Time" +"\n"+
				"2.	Host uptime"+"\n3.	Host memory use" +"\n4.	Host Netstat"+
				"\n5.	Host current users"+"\n6.	Host running processes"+
				"\n7.	Quit");
		
		Scanner sc = new Scanner(System.in);
		boolean exitApp = false;
		String inputString = "";
		
		while(!exitApp){
			
			inputString = sc.next();
			
			switch(inputString){
			
			case "1":
				getDateTime();
				break;
			case "2":
				getUptime();
				break;
			case "3":
				getMemoryUsage();
				break;
			case "4":
				getNetstat();
				break;
			case "5":
				getCurrentUsers();
				break;
			case "6":
				getRunningProcesses();
				break;
			case "7":
				exitApp = true;
				break;
			default:
				System.out.println("Invalid Entry");
			}
			
		}
	}
	
	
	
	
	public static String getDateTime(){
		
		return "Not Implemented";
	}
	
	private static String getUptime(){
			
			return "Not Implemented";
		}
	private static String getMemoryUsage(){
		
		return "Not Implemented";
	}
	private static String getNetstat(){
		
		return "Not Implemented";
	}
	private static String getCurrentUsers(){
		
		return "Not Implemented";
	}
	private static String getRunningProcesses(){
		
		return "Not Implemented";
	}

	

}
