package mysql_practica3;

import java.sql.SQLException;

import java.util.InputMismatchException;

import java.util.Scanner;


public class menu {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Scanner entrada = new Scanner(System.in);
		boolean salir = false, salir2 = false;
		conexionBD conexion = new conexionBD();
		conexion.login();
		while (!salir) {
			try {
				System.out.print("\n0. SALIR \n" + "1. Gestionar los Clientes. \n" + "2. Gestionar los Vehiculos. \n"
						+ "3. Gestionar las Ventas. \n" + "\t******ELIGE EL NUMERO QUE DESEE******: ");

				int opcion = entrada.nextInt();
				salir2 = false;
				entrada.nextLine();
				if (opcion == 0) {
					System.out.println("HEMOS SALIDO SATISFACTORIAMENTE");
					conexion.cerrarConexion();
					salir = true;
				} else if (opcion == 1) {
					while (!salir2) {
						System.out.print(
								"\n0.Volver al menu inicial\n1.Insertar Cliente\n2.Modificar Cliente\n3.Borrar Cliente\n4.Listar Clientes\n5.Ver información de un Cliente\\n\t******ELIGE EL NUMERO QUE DESEE******: ");
						int opcion2 = entrada.nextInt();

						if (opcion2 == 0) {
							salir2 = true;
						} else if (opcion2 == 1) { //hecho
							// INSERTAR CLIENTE
							conexion.insertarCliente();
						} else if (opcion2 == 2) { //hecho
							// MODIFICAR CLIENTE
							conexion.modificarCliente();
						} else if (opcion2 == 3) { //hecho
							// ELIMINAR CLIENTE
							conexion.eliminarCliente();;
						} else if (opcion2 == 4) { //hecho
							// LISTAR CLIENTES
							conexion.listarClientes();
						} else if (opcion2 == 5) { //hecho
							// VER INFORMACION DE UN CLIENTE
							conexion.info_Cliente();
						}
					}
				} else if (opcion == 2) {
					while (!salir2) {
						System.out.print(
								"\n0.Volver al menu inicial\n1.Insertar Vehiculo\n2.Modificar Vehiculo\n3.Eliminar Vehiculo\n4.Listar Vehiculos\n5.Ver información de un Vehiculo\n\t******ELIGE EL NUMERO QUE DESEE******: ");
						int opcion2 = entrada.nextInt();

						if (opcion2 == 0) {
							salir2 = true;
						} else if (opcion2 == 1) { //hecho
							// INSERTAR VEHICULO
							conexion.insertarVehiculo();
						} else if (opcion2 == 2) { //hecho
							// MODIFICAR VEHICULO
							conexion.modificarVehiculo();
						} else if (opcion2 == 3) { //hecho
							// ELIMINAR VEHICULO
							conexion.eliminarVehiculo();
						} else if (opcion2 == 4) { //hecho
							// LISTAR VEHICULOS
							conexion.listarVehiculos();
						} else if (opcion2 == 5) {
							// VER INFORMACION DE UN VEHICULO
							conexion.infoVehiculo();
						}
					}
				} else if (opcion == 3) {
					while (!salir2) {
						System.out.print(
								"\n0.Volver al menu inicial\n1.Insertar Venta\n2.Modificar Venta\n3.Eliminar Venta\n4.Listar Ventas\n5.Ver información de una Venta\n\t******ELIGE EL NUMERO QUE DESEE******: ");
						int opcion2 = entrada.nextInt();

						if (opcion2 == 0) {
							salir2 = true;
						} else if (opcion2 == 1) { //hecho
							// INSERTAR VENTA
							conexion.insertarVentas();
						} else if (opcion2 == 2) {
							// MODIFICAR VENTA
							conexion.modificarVentas();
						} else if (opcion2 == 3) {
							// ELIMINAR VENTA
							conexion.eliminarVentas();
						} else if (opcion2 == 4) {
							// LISTAR VENTA
							conexion.listarVentas();
						} else if (opcion2 == 5) {
							// VER INFORMACION DE UN VENTA
							conexion.info_ventas();
						}

					}
				} else {
					System.out.println("\nOpcion no contemplada en el menu. ");
				}
			} catch (InputMismatchException e) {
				System.err.println("\nDebes insertar un número\n");
				entrada.next();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		entrada.close();
	}

}
