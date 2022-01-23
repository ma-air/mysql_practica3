package mysql_practica3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class conexionBD {
	String db_ = "";
	String login_ = "root";
	String password_ = "root";
	String url_ = "";
	Statement st;

	final Scanner entrada = new Scanner(System.in);
	// ESTABLECEMOS LA CONEXION CON LA BASE DE DATOS
	Connection connection_;

	public void conectar(String nombreBD, String login, String password) throws SQLException {
		try {

			db_ = nombreBD;
			login_ = login;
			password_ = password;
			url_ = "jdbc:mysql://localhost/" + nombreBD;

			// ESTABLECEMOS LA CONEXION CON LA BASE DE DATOS
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection_ = DriverManager.getConnection(url_, login_, password_);

			if (connection_ != null) {
				System.out.println("Conexion establecida con la base de datos: " + db_);
			} else {
				System.err.println("SE HA PRODUCIDO UN ERROR");
			}

			Statement st = connection_.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS vehiculos " + "(matricula CHAR(7) not NULL, "
					+ " marca VARCHAR(45) NULL, " + " modelo VARCHAR(45)  NULL, " + " precioVenta FLOAT(10)  NULL, "
					+ " color VARCHAR(45)  NULL, " + " PRIMARY KEY ( matricula ))";
			String sql1 = "CREATE TABLE IF NOT EXISTS clientes " + "(nif VARCHAR(9) not NULL, "
					+ " nombre VARCHAR(20)  NULL, " + " apellido VARCHAR(45)  NULL, " + " PRIMARY KEY ( nif ))";
			String sql2 = "CREATE TABLE IF NOT EXISTS descuentos " + "(idDescuento INTEGER not NULL, "
					+ " descuento INTEGER  NULL, " + " motivoDesc INTEGER  NULL, " + " PRIMARY KEY ( idDescuento ))";
			String sql3 = "CREATE TABLE IF NOT EXISTS ventas " + "(idVenta INTEGER not NULL, " + " fecha DATE NULL, "
					+ " vehiculo_matricula CHAR(7) not NULL, " + " cliente_nif VARCHAR(9) not NULL, "
					+ " id_descuento INTEGER not NULL, " + " PRIMARY KEY ( idVenta ))";
			String sql4 = " ALTER TABLE ventas ADD CONSTRAINT FOREIGN KEY (vehiculo_matricula) REFERENCES vehiculos (matricula) ";
			String sql5 = " ALTER TABLE ventas ADD CONSTRAINT FOREIGN KEY (cliente_nif) REFERENCES clientes(nif) ";
			String sql6 = " ALTER TABLE ventas ADD CONSTRAINT FOREIGN KEY (id_descuento) REFERENCES descuentos(idDescuento) ";
			st.executeUpdate(sql);
			st.executeUpdate(sql1);
			st.executeUpdate(sql2);
			st.executeUpdate(sql3);
			st.executeUpdate(sql4);
			st.executeUpdate(sql5);
			st.executeUpdate(sql6);

		} catch (

		ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void login() throws SQLException, ClassNotFoundException {
		System.out.print("Introduce nombre de la base de datos: ");
		final String bd = entrada.next();
		System.out.print("Introduce login de mysql: ");
		final String login = entrada.next();
		System.out.print("Introduce password de mysql: ");
		final String password = entrada.next();
		createDataBase(bd, login, password);
		conectar(bd, login, password);

	}

	public void createDataBase(String nombreBD, String login, String password)
			throws ClassNotFoundException, SQLException {
		db_ = nombreBD;
		login_ = login;
		password_ = password;
		url_ = "jdbc:mysql://localhost/";

		// ESTABLECEMOS LA CONEXION CON LA BASE DE DATOS
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection_ = DriverManager.getConnection(url_, login_, password_);

		if (connection_ != null) {
		} else {
			System.err.println("SE HA PRODUCIDO UN ERROR");
		}
		Statement st = connection_.createStatement();
		String schema = "CREATE DATABASE IF NOT EXISTS " + nombreBD + ";";
		String query = "USE " + nombreBD;
		st.executeUpdate(schema);
		st.executeUpdate(query);

	}

	public void cerrarConexion() {
		try {
			connection_.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void actualizarPS(PreparedStatement ps) throws SQLException {
		int rtdo = ps.executeUpdate();
		if (rtdo > 0) {
			System.out.println("Registro guardado con éxito");
		} else {
			System.err.println("\nSe produjo algún problema");
		}
	}

	// VALIDAR EL NIF DEL CLIENTE
	public boolean validarNif(String nif) throws Exception {
		boolean aux = false;
		Pattern pat = Pattern.compile("[0-9]{8,9}[A-Z a-z]");
		Matcher mat = pat.matcher(nif);
		if (mat.matches() == false) {
			System.err.println("\tIntroduce 8 dígitos y una letra.\n");
		} else {
			aux = true;
		}
		return aux;

	}

	// VALIDAR LA MATRICULA DEL COCHE PARA QUE TENGA 4 NUMEROS DE 0 A 9 Y TRES
	// LETRAS DE A A Z
	public boolean validarMatricula(String matricula) throws Exception {

		boolean aux = false;
		Pattern pat = Pattern.compile("^[0-9]{4}[a-zA-Z]{3}$");
		Matcher mat = pat.matcher(matricula);
		if (mat.matches() == false) {
			System.err.println("\tIntroduce 4 dígitos y 3 letras.\n");
		} else {
			aux = true;
		}
		return aux;
	}

	public void insertarVehiculo() {
		try {

			String sql = "INSERT INTO `Vehiculos` (`matricula`, `marca`, `modelo`, `precio de venta`, `color`) VALUES (?, ?, ?, ?, ?);\n";
			PreparedStatement ps = connection_.prepareStatement(sql);

			System.out.print("Introduce la matricula: ");
			String matricula = entrada.next();
			if (validarMatricula(matricula)) {
				if (existeVehiculo(matricula) == false) {
					ps.setString(1, matricula);

					System.out.print("Introduce la marca: ");
					String marca = entrada.next();
					ps.setString(2, marca);

					System.out.print("Introduce el modelo: ");
					String modelo = entrada.next();
					ps.setString(3, modelo);

					System.out.print("Introduce el precio de venta: ");
					float precioVenta = entrada.nextFloat();
					ps.setFloat(4, precioVenta);
					System.out.print("Introduce el color: ");
					String color = entrada.next();
					ps.setString(5, color);

					actualizarPS(ps);
				} else {
					System.err.println("\nYa existe este vehiculo, no puede insertar uno que ya existe.");
				}
			}
			listarVehiculos();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean existeVehiculo(String matricula) throws SQLException {
		boolean aux = false;
		Statement st_ = connection_.createStatement();
		String sql = "Select * from vehiculos where matricula like '" + matricula + "'";
		ResultSet resultado = st_.executeQuery(sql);
		if (resultado.next() == true) {
			aux = true;
		}
		return aux;
	}

	public void listarVehiculos() {
		try {
			Statement st_ = connection_.createStatement();
			String sql = "Select * from vehiculos";
			ResultSet resultado = st_.executeQuery(sql);
			String tabla = "";
			while (resultado.next()) {
				String matricula = resultado.getString("matricula");
				String marca = resultado.getString("marca");
				String modelo = resultado.getString("modelo");
				String precioVenta = resultado.getString("precio de venta");
				String color = resultado.getString("color");
				tabla = matricula + "   " + marca + "  " + modelo + "  " + precioVenta + " " + color;
				System.out.println("\n" + tabla);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void modificarVehiculo() {
		try {

			System.out.print("Introduce la matricula: ");
			String matricula = entrada.next();
			if (validarMatricula(matricula)) {
				if (existeVehiculo(matricula) == true) {
					System.out.print(
							"Modificar Matricula(1) , Marca(2), Modelo(3), Precio de venta(4), Color(5). Que desea modifcar: ");
					int opcion = entrada.nextInt();
					if (opcion == 1) {
						String sql1 = "UPDATE `vehiculos` SET `matricula`= ? WHERE `matricula`= ?;\n";

						PreparedStatement ps1 = connection_.prepareStatement(sql1);
						System.out.print("Introduce la nueva matricula: ");
						String matriculaNueva = entrada.next();
						if (existeVehiculo(matriculaNueva) == false) {
							ps1.setString(1, matriculaNueva);
							ps1.setString(2, matricula);
							actualizarPS(ps1);
						} else {
							System.err.println(
									"Por favor, introduzca una matricula que no exista en nuestra Base de Datos.");
						}
					} else if (opcion == 2) {
						String sql2 = "UPDATE `vehiculos` SET `marca`= ? WHERE `matricula`= ?;\n";
						PreparedStatement ps2 = connection_.prepareStatement(sql2);
						System.out.print("Introduce la nueva marca: ");
						String marca = entrada.next();
						ps2.setString(1, marca);
						ps2.setString(2, matricula);
						actualizarPS(ps2);

					} else if (opcion == 3) {
						String sql3 = "UPDATE `vehiculos` SET `modelo`= ? WHERE `matricula`= ?;\n";
						PreparedStatement ps3 = connection_.prepareStatement(sql3);
						System.out.print("Introduce el nuevo modelo: ");
						String modelo = entrada.next();
						ps3.setString(1, modelo);
						ps3.setString(2, matricula);
						actualizarPS(ps3);

					} else if (opcion == 4) {
						String sql4 = "UPDATE `vehiculos` SET `precio de venta`= ? WHERE `matricula`= ?;\n";
						PreparedStatement ps4 = connection_.prepareStatement(sql4);
						System.out.print("Introduce el nuevo precio de venta: ");
						Float precio = entrada.nextFloat();
						ps4.setFloat(1, precio);
						ps4.setString(2, matricula);
						actualizarPS(ps4);
					} else if (opcion == 5) {
						String sql5 = "UPDATE `vehiculos` SET `color`= ? WHERE `matricula`= ?;\n";
						PreparedStatement ps5 = connection_.prepareStatement(sql5);
						System.out.print("Introduce el nuevo color: ");
						String color = entrada.next();
						ps5.setString(1, color);
						ps5.setString(2, matricula);
						actualizarPS(ps5);

					}
				}
			} else {
				System.err.println("\nPor favor, introduce una matricula de un vehiculo que exista.");
			}
			listarVehiculos();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void eliminarVehiculo() {
		try {
			System.out.print("Vehiculo a buscar (matricula): ");
			String matricula = entrada.next();
			if (existeVehiculo(matricula) == true) {
				String sql1 = "DELETE FROM `vehiculos` WHERE `matricula` LIKE ?;\n";
				PreparedStatement ps1 = connection_.prepareStatement(sql1);
				ps1.setString(1, matricula);
				actualizarPS(ps1);
			} else {
				System.err.println("\nPor favor, introduce un vehiculo con una matricula que exista.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		listarVehiculos();

	}

	public void infoVehiculo() {
		try {
			Statement st_ = connection_.createStatement();
			System.out.print("Busca el vehiculo por matricula: ");
			String matricula = entrada.next();
			if (existeVehiculo(matricula)) {
				String sql = "Select * from vehiculos where matricula like '" + matricula + "'";
				ResultSet resultado = st_.executeQuery(sql);
				String tabla = "";
				while (resultado.next()) {
					String matricula2 = resultado.getString("matricula");
					String marca = resultado.getString("marca");
					String modelo = resultado.getString("modelo");
					String precioVenta = resultado.getString("precio de venta");
					String color = resultado.getString("color");
					tabla = matricula2 + "   " + marca + "  " + modelo + "  " + precioVenta + " " + color;
					System.out.println("\n" + tabla);
				}
			} else {
				System.err.println("\nNo se ha encontrado el vehiculo que desea.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void insertarCliente() {
		try {
			String sql = "INSERT INTO `clientes` (`nif`, `nombre`, `apellido`) VALUES (?, ?, ?);\n";
			PreparedStatement ps = connection_.prepareStatement(sql);

			System.out.print("Introduce el nif: ");
			String nif = entrada.next();
			if (validarNif(nif)) {
				if (existeCliente(nif) == false) {
					ps.setString(1, nif);

					System.out.print("Introduce el nombre: ");
					String nombre = entrada.next();
					ps.setString(2, nombre);

					System.out.print("Introduce el primer apellido: ");
					String apellido = entrada.next();
					ps.setString(3, apellido);

					actualizarPS(ps);
				} else {
					System.err.println("\nYa existe este cliente, no puede insertar uno que ya existe.");
				}
			}
			listarClientes();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean existeCliente(String nif) throws SQLException {
		boolean aux = false;
		Statement st_ = connection_.createStatement();
		String sql = "Select * from clientes where nif like '" + nif + "'";
		ResultSet resultado = st_.executeQuery(sql);
		if (resultado.next() == true) {
			aux = true;
		}
		return aux;
	}

	public void listarClientes() {
		try {
			Statement st_ = connection_.createStatement();
			String sql = "Select * from clientes";
			ResultSet resultado = st_.executeQuery(sql);
			String tabla = "";
			while (resultado.next()) {
				String nif2 = resultado.getString("nif");
				String nombre = resultado.getString("nombre");
				String apellido = resultado.getString("apellido");
				tabla = nif2 + "   " + nombre + "  " + apellido;
				System.out.println("\n" + tabla);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void modificarCliente() {
		try {

			System.out.print("Introduce el nif: ");
			String nif = entrada.next();
			if (validarNif(nif)) {
				if (existeCliente(nif) == true) {
					System.out.print("Modificar nif(1) , nombre(2), apellido(3). Que desea modifcar: ");
					int opcion = entrada.nextInt();
					if (opcion == 1) {
						String sql1 = "UPDATE `clientes` SET `nif`= ? WHERE `nif`= ?;\n";

						PreparedStatement ps1 = connection_.prepareStatement(sql1);
						System.out.print("Introduce el nuevo nif: ");
						String nif2 = entrada.next();
						if (existeCliente(nif2) == false) {
							ps1.setString(1, nif2);
							ps1.setString(2, nif);
							actualizarPS(ps1);
						} else {
							System.err.println("Por favor, introduzca un nif que no exista en nuestra Base de Datos.");
						}
					} else if (opcion == 2) {
						String sql2 = "UPDATE `clientes` SET `nombre`= ? WHERE `nif`= ?;\n";
						PreparedStatement ps2 = connection_.prepareStatement(sql2);
						System.out.print("Introduce el nuevo nombre ");
						String nombre = entrada.next();
						ps2.setString(1, nombre);
						ps2.setString(2, nif);
						actualizarPS(ps2);

					} else if (opcion == 3) {
						String sql3 = "UPDATE `clientes` SET `apellido`= ? WHERE `nif`= ?;\n";
						PreparedStatement ps3 = connection_.prepareStatement(sql3);
						System.out.print("Introduce el nuevo apellido: ");
						String apellido = entrada.next();
						ps3.setString(1, apellido);
						ps3.setString(2, nif);
						actualizarPS(ps3);

					}
				}
			} else {
				System.err.println("\nPor favor, introduce un nif de un cliente que exista.");
			}
			listarClientes();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void eliminarCliente() {
		try {
			System.out.print("Cliente a buscar (nif): ");
			String nif = entrada.next();
			if (existeCliente(nif) == true) {
				String sql1 = "DELETE FROM `clientes` WHERE `nif` LIKE ?;\n";
				PreparedStatement ps1 = connection_.prepareStatement(sql1);
				ps1.setString(1, nif);
				actualizarPS(ps1);
			} else {
				System.err.println("\nPor favor, introduce un cliente con NIF/ DNI que exista.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		listarClientes();

	}

	public void info_Cliente() {
		try {
			Statement st_ = connection_.createStatement();
			System.out.print("Busca el cliente por nif: ");
			String nif = entrada.next();
			if (existeCliente(nif) == true) {
				String sql = "Select * from clientes where nif like '" + nif + "'";
				ResultSet resultado = st_.executeQuery(sql);
				String tabla = "";
				while (resultado.next()) {
					String nif2 = resultado.getString("nif");
					String nombre = resultado.getString("nombre");
					String apellido = resultado.getString("apellido");
					tabla = nif2 + "   " + nombre + "  " + apellido;
					System.out.println("\n" + tabla);
				}
			} else {
				System.err.println("\nNo se ha encontrado el cliente que desea.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void insertarVentas() {
		try {
			String sql = "INSERT INTO `ventas` (`idVenta`, `fecha`, `vehiculo_matricula`, `cliente_nif`, `id_descuento`) VALUES (?, ?, ?, ?, ?);\n";
			PreparedStatement ps = connection_.prepareStatement(sql);

			System.out.print("Introduce el id: ");
			int id = entrada.nextInt();
			if (existeVenta(id) == false) {
				ps.setInt(1, id);

				Date fechita = conseguirFechaSinRayarse();
				ps.setDate(2, fechita);

				System.out.print("Introduce la matricula del coche: ");
				String matricula = entrada.next();
				if (validarMatricula(matricula)) {
					if (existeVehiculo(matricula)) {
						ps.setString(3, matricula);
					} else {
						System.err.println("Introduce un vehiculo que exista en nuestra base de datos.");
					}
				} else {
					System.err.println("\tIntroduce 4 dígitos y 3 letras.\n");
				}

				System.out.print("Introduce el nif del cliente: ");
				String nif = entrada.next();
				if (validarNif(nif)) {
					if (existeCliente(nif)) {
						ps.setString(4, nif);
					} else {
						System.err.println("Introduce un cliente que exista.");
					}
				} else {
					System.err.println("Introduce un nif correctamente.");
				}

				System.out.print("Introduce el id del descuento: ");
				int id_desc = entrada.nextInt();
				ps.setInt(5, id_desc);

				actualizarPS(ps);
			} else {
				System.err.println("\nYa existe esta venta, no puede insertar una que ya existe.");
			}

			listarVentas();
		} catch (

		SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Date conseguirFechaSinRayarse() throws ParseException, InputMismatchException {
		System.out.print("Introduzca la fecha (yyyy-MM-dd): ");
		String str = entrada.next();
		Date date = Date.valueOf(str);
		return date;
	}

	public boolean existeVenta(int id) throws SQLException {
		boolean aux = false;
		Statement st_ = connection_.createStatement();
		String sql = "Select * from ventas where idVenta like '" + id + "'";
		ResultSet resultado = st_.executeQuery(sql);
		if (resultado.next() == true) {
			aux = true;
		}
		return aux;
	}

	public void listarVentas() {
		try {
			Statement st_ = connection_.createStatement();
			String sql = "Select * from ventas";
			ResultSet resultado = st_.executeQuery(sql);
			String tabla = "";
			while (resultado.next()) {
				String id = resultado.getString("idVenta");
				String fecha = resultado.getString("fecha");
				String Vehiculo_matricula = resultado.getString("Vehiculo_matricula");
				String Cliente_nif = resultado.getString("Cliente_nif");
				String id_descuento = resultado.getString("id_descuento");
				tabla = id + "   " + fecha + "  " + Vehiculo_matricula + "  " + Cliente_nif + "  " + id_descuento;
				System.out.println("\n" + tabla);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void modificarVentas() {
		try {

			System.out.print("Introduce el id: ");
			int id = entrada.nextInt();
			if (existeVenta(id) == true) {
				System.out.print(
						"Modificar id(1) , fecha(2), matricula del vehiculo(3), nif del cliente(4), id de descuento(5). Que desea modifcar: ");
				int opcion = entrada.nextInt();
				if (opcion == 1) {
					String sql1 = "UPDATE `ventas` SET `idVenta`= ? WHERE `idVenta`= ?;\n";

					PreparedStatement ps1 = connection_.prepareStatement(sql1);
					System.out.print("Introduce el nuevo id: ");
					int id2 = entrada.nextInt();
					if (existeVenta(id2) == false) {
						ps1.setInt(1, id2);
						ps1.setInt(2, id);
						actualizarPS(ps1);
					} else {
						System.err.println("Por favor, introduzca un id que no exista en nuestra Base de Datos.");
					}
				} else if (opcion == 2) {
					String sql2 = "UPDATE `ventas` SET `fecha`= ? WHERE `idVenta`= ?;\n";
					PreparedStatement ps2 = connection_.prepareStatement(sql2);
					Date fechita = conseguirFechaSinRayarse();
					ps2.setDate(1, fechita);
					ps2.setInt(2, id);
					actualizarPS(ps2);

				} else if (opcion == 3) {
					String sql3 = "UPDATE `ventas` SET `Vehiculo_matricula`= ? WHERE `idVenta`= ?;\n";
					PreparedStatement ps3 = connection_.prepareStatement(sql3);
					System.out.print("Introduce la matricula del coche: ");
					String matricula = entrada.next();
					ps3.setString(1, matricula);
					ps3.setInt(2, id);
					actualizarPS(ps3);

				} else if (opcion == 4) {
					String sql4 = "UPDATE `ventas` SET `Cliente_nif`= ? WHERE `idVenta`= ?;\n";
					PreparedStatement ps4 = connection_.prepareStatement(sql4);
					System.out.print("Introduce el nif del cliente: ");
					String nif = entrada.next();
					ps4.setString(1, nif);
					ps4.setInt(2, id);
					actualizarPS(ps4);
				} else if (opcion == 5) {
					String sql5 = "UPDATE `ventas` SET `id_descuento`= ? WHERE `idVenta`= ?;\n";
					PreparedStatement ps5 = connection_.prepareStatement(sql5);
					System.out.print("Introduce el id del desciento: ");
					int id_desc = entrada.nextInt();
					ps5.setInt(1, id_desc);
					ps5.setInt(2, id);
					actualizarPS(ps5);
				}

			} else {
				System.err.println("\nPor favor, introduce un id de una venta que exista.");
			}
			listarVentas();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void eliminarVentas() {
		try {
			System.out.print("Introduce el id: ");
			int id = entrada.nextInt();
			if (existeVenta(id) == true) {
				String sql1 = "DELETE FROM `ventas` WHERE `idVenta` LIKE ?;\n";
				PreparedStatement ps1 = connection_.prepareStatement(sql1);
				ps1.setInt(1, id);
				actualizarPS(ps1);
			} else {
				System.err.println("\nPor favor, introduce un venta con id que exista.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		listarVentas();

	}

	public void info_ventas() {
		try {
			Statement st_ = connection_.createStatement();
			System.out.print("Introduce el id: ");
			int id = entrada.nextInt();
			if (existeVenta(id) == true) {
				String sql = "Select * from ventas where idVenta like '" + id + "'";
				ResultSet resultado = st_.executeQuery(sql);
				String tabla = "";
				while (resultado.next()) {
					String id2 = resultado.getString("idVenta");
					String fecha = resultado.getString("fecha");
					String Vehiculo_matricula = resultado.getString("Vehiculo_matricula");
					String Cliente_nif = resultado.getString("Cliente_nif");
					String id_descuento = resultado.getString("id_descuento");
					tabla = id + "   " + fecha + "  " + Vehiculo_matricula + "  " + Cliente_nif + "  " + id_descuento;
					System.out.println("\n" + tabla);
				}
			} else {
				System.err.println("\nNo se ha encontrado el numero de venta que desea.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
