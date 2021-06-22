package at.ac.fhcampuswien.newsanalyzer.ui;


import at.ac.fhcampuswien.newsanalyzer.ctrl.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInterface
{
	private String previousSearch = "corona";
	private Controller ctrl = new Controller();

	public void getDataFromCtrl1(){
		System.out.println("Looking for articles with word \"Corona\"");
		this.previousSearch = "corona";

		ctrl.process("corona");
	}

	public void getDataFromCtrl2(){
		System.out.println("Looking for articles with word \"Wirtschaft\"");
		this.previousSearch = "Wirtschaft";

		ctrl.process("Wirtschaft");
	}

	public void getDataFromCtrl3(){
		System.out.println("Looking for articles with word \"Krieg\"");
		this.previousSearch = "Krieg";

		ctrl.process("Krieg");
	}

	public void getDataForCustomInput() {
		System.out.print("Enter the input you want to find: ");
		String newCustomLine = readLine();

		System.out.println("Looking for articles with word \"" + newCustomLine + "\"");
		this.previousSearch = newCustomLine;

		ctrl.process(newCustomLine);
	}

	public void getDataFromLastSearch(){
		System.out.println("Looking for articles with word \"" + this.previousSearch + "\"");
		ctrl.process(this.previousSearch);
	}


	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interface");

		menu.setTitle("Wählen Sie aus:");
		menu.insert("a", "Choice Corona", this::getDataFromCtrl1);
		menu.insert("b", "Choice Wirtschaft", this::getDataFromCtrl2);
		menu.insert("c", "Choice Krieg", this::getDataFromCtrl3);
		menu.insert("d", "Choice User Input:",this::getDataForCustomInput);
		menu.insert("e", "Download last search", this::getDataFromLastSearch);
		menu.insert("q", "Quit", null);

		Runnable choice;
		while ((choice = menu.exec()) != null) {
			choice.run();
		}
		System.out.println("Program finished");
	}


	protected String readLine() {
		String value = "\0";
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = inReader.readLine();
		} catch (IOException ignored) {
		}
		return value.trim();
	}

	protected Double readDouble(int lowerlimit, int upperlimit) 	{
		Double number = null;
		while (number == null) {
			String str = this.readLine();
			try {
				number = Double.parseDouble(str);
			} catch (NumberFormatException e) {
				number = null;
				System.out.println("Please enter a valid number:");
				continue;
			}
			if (number < lowerlimit) {
				System.out.println("Please enter a higher number:");
				number = null;
			} else if (number > upperlimit) {
				System.out.println("Please enter a lower number:");
				number = null;
			}
		}
		return number;
	}
}
