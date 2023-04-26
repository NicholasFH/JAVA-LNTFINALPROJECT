package Len;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

//import application.Database;
//import application.Exception;

class Menu {
	public Integer code;
	public String name;
	public Float price;
	public Integer stock;

	/**
	 * @param code
	 * @param name
	 * @param price
	 * @param stock
	 */
	public Menu(Integer code, String name, Float price, Integer stock) {
		this.code = code;
		this.name = name;
		this.price = price;
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "Menu [code=" + code + ", name=" + name + ", price=" + price + ", stock=" + stock + "]";
	}

	/**
	 *
	 * @param name
	 * @param price
	 * @param stock
	 * @apiNote Use this to create a new `Menu`.
	 * @return
	 */
	public static Menu create(String name, Float price, Integer stock) {
		Menu menu = new Menu(0x00, name, price, stock);

		// Generate random id
		menu.code = ThreadLocalRandom.current().nextInt(100, 999);

		return menu;
	};

}

class Database {
	public Connection connection;

	// Methods
	/**
	 *
	 * @param new_menu
	 * @apiNote Returns `False` if something went wrong, use it.
	 * @return
	 */
	public Boolean insert(Menu new_menu) {
		try {
			Statement statement = this.connection.createStatement();
			statement.executeUpdate("INSERT INTO menu " + String.format("VALUES (%s, '%s', %f, %s)", new_menu.code,
					new_menu.name, new_menu.price, new_menu.stock));

			return true;
		} catch (SQLException e) {
			System.out.println("[Database] Failed to insert new menu!");
		}

		return false;
	}

	public ArrayList<Menu> get() {
		ArrayList<Menu> menus = new ArrayList<Menu>();

		try {
			Statement statement = this.connection.createStatement();

			ResultSet results = statement.executeQuery("SELECT * FROM menu;");

			while (results.next()) {
				menus.add(new Menu(results.getInt(1), results.getString(2), results.getFloat(3), results.getInt(4)));
			}
		} catch (SQLException e) {
			System.out.println("[Database] Failed to get menu from database!!!");
			System.out.println("[Reason] " + e.getMessage());
		}

		System.out.println(menus);

		return menus;
	}

	public Boolean update(Menu updated_menu) {
		try {
			Statement statement = this.connection.createStatement();
			statement
					.executeUpdate(
							"UPDATE menu SET "
									+ String.format("name='%s', price=%f, stock=%s ", updated_menu.name,
											updated_menu.price, updated_menu.stock)
									+ "WHERE code=" + updated_menu.code.toString() + ";");

			return true;
		} catch (SQLException e) {
			System.out.println("[Database] Failed to update menu!!");
		}

		return false;
	}

	public Boolean delete(Integer menu_id) {
		try {
			Statement statement = this.connection.createStatement();
			statement.executeUpdate("DELETE FROM menu WHERE code=" + menu_id.toString() + ";");

			return true;

		} catch (SQLException e) {
			System.out.println("[Database] Failed to delete menu!!!");
		}

		return false;
	}

	// Factory
	public static Database create() throws Exception {
		Database db = new Database();

		try {
			db.connection = DriverManager.getConnection("jdbc:mysql://localhost/project?" + "user=root&password=");
		} catch (Exception err) {
			System.out.println("[Error] Failed to connect to the database!");
			System.out.println("[Reason] " + err.getMessage());

			throw new Exception("Fix database!!!");
		}

		System.out.println("[Database] Connected!");

		return db;
	}
}

public class Interface {
	public static void main(String[] args) {
		System.out.println("[System] Java version is " + System.getProperty("java.version"));

		Database database;
		try {
			database = Database.create(); // connects to db
		} catch (Exception e) {
			System.out.println("[DB] Failed to connect to the database: " + e.getMessage());
			return;
		}

		// for example if you want to make a new menu.
		Menu menu = Menu.create("test", 420.0f, 69420);

		// Add it into the database
		if (database.insert(menu)) {
			System.out.println("[DB] Added " + menu.name + " into the database!");
		} else {
			System.out.println("[DB] " + menu.name + " already exists in the database!");
		}

		// then to update it
		menu.price = 999f;
		menu.stock = 2;

		if (database.update(menu)) {
			System.out.println("[DB] Updated " + menu.name + "!");
		} else {
			System.out.println("[DB] " + menu.name + " Failed to update!");
		}

		// then to remove
//		if (database.delete(menu.code)) {
//			System.out.println("[DB] Deleted " + menu.name + "!");
//		} else {
//			System.out.println("[DB] " + menu.name + " failed to delete!");
//		}

//		database.delete(520)
		
	}
}


//supposed to be the GUI but failed

////public class Interface {
//	// Internal
////	public Database database;
//
////	// User-Interface(s)
////	public InterfaceController controller;
////	public Stage screen;
////	public TableView<Menu> menu_table;
//
////	 Methods
////	public void connect_to_database() throws Exception {
////		this.database = Database.create();
////	}
//
////	@SuppressWarnings("rawtypes")
////	public void bind_user_interface() {
////		Scene scene = this.screen.getScene();
////
////		// Table
////		this.menu_table = (TableView) scene.lookup("#menu_table");
////
////	}
////
////	// Utils
////	public void populate_user_interface() {
////		ArrayList<Menu> menus = this.database.get();
////
////		System.out.println("[GUI] Populating User Interface!");
////		for (Menu menu : menus) {
////			System.out.println("[Database] " + menu.toString());
////			this.menu_table.getItems().add(menu);
////		}
////	}
////
////	// Factory(s)
////	@Override
////	public void start(Stage screen) throws Exception {
////		// Test
////		System.out.println(Menu.create("anything", 4.20f, 69).toString());
////
////		// Gui
////		this.screen = screen;
////
////		URL url = new File("src/application/resources/application.fxml").toURI().toURL();
////		FXMLLoader loader = new FXMLLoader(url);
////		Scene scene = new Scene(loader.load());
////
////		// Start
////		screen.setScene(scene);
////		screen.show();
////
////		// Controller
////		this.controller = (InterfaceController) loader.getController();
////
////		this.connect_to_database();
////		this.bind_user_interface();
////		this.populate_user_interface();
////
////		System.out.println(this.controller.menu_table);
////	}
