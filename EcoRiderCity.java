package uva.poo.practica2;

import fabricante.externo.tarjetas.TarjetaMonedero;
import uva.poo.practica2.Plaza.EstadoPlaza;

import java.util.ArrayList;
import java.util.List;

/**
 * La clase 'EcoRiderCity' es el sistema general de control de los EcoRiderParking <br>
 * 
 * Representa la funcionalidad y cohesion entre los diferentes parkings <br>
 * de EcoRiderParking, por lo que tambien la funcionalidad de plaza.<br>
 * La idea principal de uso de esta aplicacion es la siguiente<br>
 * <ul>
 * <li>Alquilar un vehiculo
 * <li>GetPagoAlquiler
 * <li>DescontarSaldoDeLaTareta con el resultado de getPagoAlquiler
 * <li>Aparcar el vehiculo
 * </ul>
 * Debe tener como minimo estas funcionalidades:
 * <ul>
 * <li>Añadir un nuevo aparcamiento.
 * <li>Eliminar un aparcamiento.
 * <li>Modificar la cantidad establecida para la fianza.
 * <li>Obtener todos los aparcamientos de EcoRiderSharing que hay en la ciudad.
 * <li>Obtener todos los aparcamientos operativos.
 * <li>Obtener todos los aparcamientos completos.
 * <li>Obtener todos los aparcamientos en los que haya sitio donde devolver un vehiculo.
 * <li>Obtener todos los aparcamientos cercanos a una coordenada GPS,
 *  donde se pueda dejar unvehiculo dentro de un radio.
 * <li>Obtener todos los aparcamientos que tengan alguna plaza inoperativa.
 * <li>Inicializar una tarjeta sin saldo.
 * <li>Inicializar una tarjeta determinando el saldo.
 * <li>Recargar una tarjeta.
 * <li>Descontar saldo de una tarjeta.
 * <li>Obtener saldo.
 * <li>Ampliar un parking con un numero de plazas con cargador rapido, semirapido y sin cargador
 * <li>Obtener una plaza con Cargador
 * <li>Conocer si una plaza tiene Cargador
 * <li>Aparcar un vehiculo si es compatible con la plaza
 * <li>Eliminar un vehiculo por Id
 * <li>Calcular los kmrealizados y kmtotales de un vehiculo
 * <li>Conocer si una plaza admite un vehiculo
 * <li>Conocer marca, modelo, matricula de un vehiculo
 * <li>Obtener el vehiculo que se encuentra en una plaza
 * <li>A partir de una plaza obtener al parking al que pertenece
 * </ul>
 * 
 * Ademas hemos añadido:
 * <ul>
 * <li>Obtener parkings con alguna plaza inoperativa.
 * <li>Obtener parkings Inoperativos, es decir, con todas las plazas inoperativas.
 * <li>Obtener parkings cercanos a una coordenada GPS cualquiera, dentro de un radio.
 * <li>Obtener Parking por Id.
 * </ul>
 * 
 * 
 * @author alvgome
 * @author quegome
 *
 */
public class EcoRiderCity {
	
	private static final String ID_PARKING_MENOR_QUE_CERO = "Un parking no puede tener un id negativo";
	private static final String ID_PLAZA_MENOR_QUE_CERO = "Un plaza no puede tener un id negativo";
	private static final String NO_EXISTE_PARKING = "No existe parking con ese id en la ciudad";
	
	private ArrayList<EcoRiderParking> parkings = new ArrayList<>();
	private ArrayList<Vehiculo> vehiculos = new ArrayList<>();
	private double fianza;
	private int numParkings;
	
	
	/**
	 * Crea una ciudad con fianza inicial.
	 *
	 * @param fianza importe de fianza (no negativo)
	 * @throws IllegalArgumentException si {@code fianza < 0} o {@code radio < 0}
	 */
	public EcoRiderCity(double fianza) {
	    setFianza(fianza);
	}

	/**
     * Inicializa una tarjeta sin saldo (saldo 0).
     *
     * <p>Valida previamente la credencial con
     * {@link #validarCredencialInicioTarjeta(String)}.</p>
     *
     * @param credencial credencial del cliente (no nula)
     * @return la tarjeta creada y registrada internamente
     * @throws IllegalArgumentException si la credencial no es correcta
     */
	public TarjetaMonedero inicializarTarjetaSaldoCero(String credencial) {
		validarCredencialInicioTarjeta(credencial);
		return new TarjetaMonedero(credencial);
	}
	
	/**
     * Inicializa una tarjeta con el saldo indicado.
     *
     * @param credencial credencial del cliente (no nula)
     * @param saldoInicial saldo inicial (no negativo)
     * @return la tarjeta creada y registrada internamente
     * @throws IllegalArgumentException si la credencial no es correcta
     *                                  o si {@code saldoInicial < 0}
     */
	public TarjetaMonedero inicializarTarjetaSaldoIndicado(String credencial, double saldoInicial) {
		
		validarCredencialInicioTarjeta(credencial);
		
		if(saldoInicial < 0)
			throw new IllegalArgumentException("El saldo inicial no puede ser negativo");
		
		return new TarjetaMonedero(credencial, saldoInicial);
	}
	
	/**
     * Devuelve el saldo actual de una tarjeta.
     *
     * @param tarjeta tarjeta a consultar (no nula)
     * @return saldo actual de la tarjeta
     * @throws NullPointerException si {@code tarjeta == null}
     */
	public double obtenerSaldoTarjeta( TarjetaMonedero tarjeta) {
		
		if(tarjeta == null) 
			throw new NullPointerException("No se puede consultar el saldo de una tarjeta null");
		
		return tarjeta.getSaldoActual();
	}
	
	/**
     * Recarga saldo en una tarjeta.
     *
     * @param tarjeta tarjeta a recargar (no nula)
     * @param credencial credencial del cliente (validada)
     * @param cantidad importe a recargar (estrictamente mayor que 0)
     * @throws NullPointerException si {@code tarjeta == null}
     * @throws IllegalArgumentException si la credencial no es válida
     *                                  o {@code cantidad < 1}
     */
	public void recargarSaldoTarjeta(TarjetaMonedero tarjeta, String credencial, double cantidad) {
		
		if(tarjeta == null) 
			throw new NullPointerException("No se puede recargar el saldo de una tarjeta null");
		
		validarCredencialInicioTarjeta(credencial);
		
		if(cantidad < 1)
			throw new IllegalArgumentException("La cantidad a recargar no puede ser igual o menor que 0");
		
		tarjeta.recargaSaldo(credencial, cantidad);
		
	}
	
	/**
     * Descuenta saldo de una tarjeta.
     *
     * @param tarjeta tarjeta a cargar (no nula)
     * @param credencial credencial de autorización especial requerida
     * @param cantidad importe a descontar (estrictamente mayor que 0
     *                 y no superior al saldo actual)
     * @throws NullPointerException si {@code tarjeta == null}
     * @throws IllegalArgumentException si la credencial no es correcta,
     *                                  si {@code cantidad < 1} o
     *                                  si {@code cantidad > tarjeta.getSaldoActual()}
     */
	public void descontarSaldoTarjeta(TarjetaMonedero tarjeta, String credencial, double cantidad) {
		if(tarjeta == null) 
			throw new NullPointerException("No se puede recargar el saldo de una tarjeta null");
		
		if(!credencial.equals("6Z1y00Nm31aA-571"))
				throw new IllegalArgumentException("La credencial que aporta el cliente para descontar saldo de la tarjeta no es correcta.");
		
		if(cantidad < 1)
			throw new IllegalArgumentException("La cantidad es menor o igual que cero.");
		
		if(cantidad > tarjeta.getSaldoActual())
			throw new IllegalArgumentException("La cantidad es mayor que el saldo actual.");
		
		tarjeta.descontarDelSaldo(credencial, cantidad);
	}
	
	/**
     * Establece el número de aparcamientos registrados.
     *
     * <p>Este valor se usa como contador interno y debe ser coherente con
     * el tamaño de la lista de {@code parkings}.</p>
     *
     * @param numParkings número de aparcamientos (no negativo)
     * @throws IllegalArgumentException si {@code numParkings < 0}
     */
	public void setNumParkings(int numParkings) {
		if(numParkings <0)
			throw new IllegalArgumentException("El numero de aparcamienntos no puede ser menor que 0");
		this.numParkings = numParkings;
	}
	
	/**
     * Va asignando los id a los parking correspondientes
     *
     * @param id identificado que se le asignara al parking
     * @param nuevo parking al que se le asignara el id
     */
	public void setIdParking(int id, EcoRiderParking nuevo) {
		for(EcoRiderParking parking : parkings) {
			if(parking.getId() == id)
				throw new IllegalArgumentException("No puede poner ese id al parking");
		}
		nuevo.setId(id);
		
	}
	
	/**
     * Establece el importe de la fianza.
     *
     * @param fianza importe de fianza (no negativo)
     * @throws IllegalArgumentException si {@code fianza < 0}
     */
    public void setFianza(double fianza) {
    	if (fianza < 0)
    		throw new IllegalArgumentException("La fianza no puede ser negativa");
    	this.fianza = fianza;
    }
    /**
     * Sustituye el conjunto de aparcamientos por la lista proporcionada.
     *
     * <p>La lista interna se limpia y se añaden las referencias de
     * {@code parkings}. A continuación se actualizan {@code numParkings}</p>
     *
     * @param parkings lista de aparcamientos (no nula; sus elementos no deben ser nulos)
     */
	public void setParkings(List<EcoRiderParking> parkings) {
		this.parkings.clear();
		this.parkings.addAll(parkings);
		setNumParkings(parkings.size());
	}
	/**
     * Cambia la ubicación de un aparcamiento por su id.
     *
     * @param ubicacion nueva ubicación (no nula)
     * @param id identificador del aparcamiento a modificar
     * @throws IllegalStateException si no hay aparcamientos registrados
     * @throws IllegalArgumentException si no existe un aparcamiento con ese id
     */
	public void setUbicacionParking(CoordenadasGPS ubicacion, int id) {
		
		if(parkings.isEmpty())
			throw new IllegalStateException("No hay parkings en la ciudad");
		
		boolean existe= false;
		for(EcoRiderParking parking : parkings) {
			if(parking.getId() == id) {
				parking.setUbicacion(ubicacion);
				existe = true;
			}
		}
		if(!existe)
			throw new IllegalArgumentException("No existe parking con ese id");
		
	}
	/**
     * Añade un nuevo aparcamiento a la ciudad.
     *
     * <p>No se permiten dos aparcamientos con la misma ubicación, ni dos parkings,
     * con el mismo identificador.</p>
     *
     * @param id identificador que se le asigna a ese parking
     * @param nuevo aparcamiento a añadir (no nulo)
     * @throws NullPointerException si {@code nuevo == null}
     * @throws IllegalArgumentException si ya existe un aparcamiento en la misma ubicación
     */
	public void annadirNuevoParking(EcoRiderParking nuevo, int id) {
		
		if (nuevo == null)
			throw new NullPointerException("No puede ser nulo el parking a añadir");
		
		for(int i=0; i<parkings.size(); i++) {
			if(nuevo.getUbicacion().equals(obtenerParkingPosicionI(i).getUbicacion()))
				throw new IllegalArgumentException("Ya existe un parking en esa ubicacion");
		}
		
		setIdParking(id, nuevo);
		parkings.add(nuevo);
		setNumParkings(parkings.size());
	}
	/**
     * Elimina un aparcamiento por id.
     *
     * @param id identificador del aparcamiento (mayor que 0)
     * @throws IllegalArgumentException si {@code id < 0} o no existe aparcamiento con ese id
     */
	public void eliminarParking(int id) {
		if (id < 0 )
			throw new IllegalArgumentException("El id tiene que ser mayor a 0");
		
		for (int i=0; i<parkings.size(); i++) {
			if(obtenerParkingPosicionI(i).getId() == id) {
				parkings.remove(i);
				setNumParkings(parkings.size());
				return;
			}
		}
		throw new IllegalArgumentException("No hay ningun parking con ese id");
	}
	/**
	 * Permite asignar un id a un vehiculo desde EcoRiderCity
	 * @param id int identificador unico del vehiculo
	 * @param vehiculo al que se le va a asignar el id
	 * @throws IllegalArgumentException si el id ya esta en uso en otro vehiculo
	 */
	public void setIdVehiculo(int id,Vehiculo vehiculo) {
		for(Vehiculo v: vehiculos) {
			if(v.getId() == id)
				throw new IllegalArgumentException("Ese id para el vehiculo ya esta en uso");
		}
		vehiculo.setId(id);
	}
	/**
	 * Permite añadir un vehiculo nuevo a una plaza de un parking especifico. 
	 * Ademas, se le asigna un id al vehiculo.
	 * 
	 * @param vehiculo que no puede ser null
	 * @param id int identificador unico del vehiculo
	 * @param idParking del parking en el que se quiere anadir el vehiculo
	 * @param idPlaza de la plaza en la se quiere anadir el vehiculo
	 * @throws NullPointerException si el vehiculo es null
	 * @throws IllegalStateException si la plaza no esta libre o no es compatible con el tipo de vehiculo
	 */
	public void annadirNuevoVehiculo(Vehiculo vehiculo, int id, int idParking, int idPlaza) {
		if(vehiculo == null)
			throw new NullPointerException("No puede ser null el vehiculo a añadir");
			
		for(int i=0; i < parkings.size(); i++) {
			if(idParking == parkings.get(i).getId()) {
				if(!parkings.get(i).estaLibre(idPlaza) || !parkings.get(i).admiteVehiculo(idPlaza, vehiculo)) {
					throw new IllegalStateException("No se pude inicializar un vehiculo en una plaza que no esta Libre o que no es compatible");
				}
				parkings.get(i).aparcarVehiculo(idPlaza, vehiculo);
			}
		}
		setIdVehiculo(id, vehiculo);
		vehiculos.add(vehiculo);
	}
	
	/**
	 * Elimina un vehiculo de EcoRiderCity mediante un id, comprueba si dicho vehiculo no esta en marcha,
	 * si existe un vehiculo con ese id y finalmente marca la plaza como vacia en la que se elimino dicho vehiculo
	 * @param id int identificador unico de cada vehiculo
	 * @throws IllegalStateException Si el vehiculo que se prentende eliminar esta en marcha
	 * @throws IllegalArgumentException Si no existe ningun vehiculo con ese id
	 * 
	 */
	public void eliminarVehiculo(int id) {
		
		boolean eliminado = false;
		Vehiculo objetivo = null;
		
		for(int i=0; i< vehiculos.size(); i++) {
			if(vehiculos.get(i).getId() == id) {
				if(vehiculos.get(i).estaEnMarcha())
					throw new IllegalStateException("No se puede eliminar un vehiculo que esta en marcha");
				
				objetivo = vehiculos.get(i);
				vehiculos.remove(i);
				eliminado = true;
			}
		}
		
		
		if(!eliminado)
			throw new IllegalArgumentException("No existe vehiculo con ese id");
		
		for(EcoRiderParking p: parkings) {
			for(Plaza pl: p.getPlazas()) {
				if(pl.getVehiculo() == objetivo) {
					pl.marcarPlazaVacia();
					return;
				}
			}
		}
	}

	/**
	 * Permite conocer el estado actual de una plaza que se encuentra en un parking
	 * @param idParking identificador del aparcamiento (mayor que 0)
	 * @param idPlaza identificador de la plaza (mayor que 0)
	 * @return Estado de la plaza
	 * @throws IllegalArgumentException si {@code idParking <= 0}
	 * @throws IllegalArgumentException si {@code idPlaza <= 0}
	 * @throws IllegalArgumentException si no existe un aparcamiento con ese id
	 * @throws IllegalArgumentException si no existe una plaza con ese id en el aparcamiento
	 *                                  (excepción propagada de {@link EcoRiderParking#conocerEstado(int)})
	 */
	public EstadoPlaza conocerEstadoPlazaEnParking(int idParking, int idPlaza) {
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		if(idPlaza < 0)
			throw new IllegalArgumentException(ID_PLAZA_MENOR_QUE_CERO);
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				return p.conocerEstado(idPlaza);
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	/**
	 * Recorre la lista de plazas y devuelve en el caso de que existan la primera vacia,
	 * en el caso de que no haya ninguna vacia devuelve una excepcion.
	 * @param idParking identificador del aparcamiento (mayor que 0)
	 * @return plaza vacia en caso de que exista una plaza libre
	 * @throws IllegalArgumentException si {@code idParking <= 0}
	 * @throws IllegalArgumentException en caso que no exista un parking con ese id
	 */
	public Plaza obtenerPlazaLibreEnParkingYPlaza(int idParking) {
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				return p.obtenerPlazaLibre();
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	/**
	 * Marca la plaza de la cual se ha pasado id como inoperativa, en el caso 
	 * de que no exista plaza con ese id devolvera una excepcion
	 * @param idParking identificador del aparcamiento (mayor que 0)
	 * @param idPlaza identificador de la plaza (mayor que 0) 
	 * @throws IllegalArgumentException si {@code idParking <= 0}
	 * @throws IllegalArgumentException si {@code idPlaza <= 0}
	 * @throws IllegalArgumentException si no existe un aparcamiento con ese id
	 * @throws IllegalArgumentException si no existe una plaza con ese id en el aparcamiento
	 *                                  (excepción propagada de {@link EcoRiderParking#marcarPlazaInoperativa(int)})
	 */
	public void marcarPlazaInoperativaDeParkingYPlaza(int idParking, int idPlaza) {
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		if(idPlaza < 0)
			throw new IllegalArgumentException(ID_PLAZA_MENOR_QUE_CERO);
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				p.marcarPlazaInoperativa(idPlaza);
				return;
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}

	
	/**
	 * Marca la plaza de la cual se ha pasado id como reservada
	 * 
	 * @param idParking identificador del aparcamiento (mayor que 0)
	 * @param idPlaza identificador de la plaza (mayor que 0)
	 * @throws IllegalArgumentException si {@code idParking <= 0}
	 * @throws IllegalArgumentException si {@code idPlaza <= 0}
	 * @throws IllegalArgumentException si no existe un aparcamiento con ese id
	 * @throws IllegalArgumentException si no existe una plaza con ese id en el aparcamiento
	 *                                  (excepción propagada de {@link EcoRiderParking#reservarVehiculo(int)})
	 */
	public void reservarVehiculoEnParkingYPlaza(int idParking, int idPlaza) {
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		if(idPlaza < 0)
			throw new IllegalArgumentException(ID_PLAZA_MENOR_QUE_CERO);
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				p.reservarVehiculo(idPlaza);
				return;
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	/**
	 * 
	 * Marca la plaza de la cual se ha pasado id como reparada en el caso de que dicha plaza estuviera como inoperativa,
	 * en el caso de que no exista plaza con ese id devolvera una excepcion
	 * 
	 * @param idParking identificador del aparcamiento (mayor que 0)
	 * @param idPlaza identificador de la plaza (mayor que 0)
	 * @throws IllegalArgumentException si {@code idParking <= 0}
	 * @throws IllegalArgumentException si {@code idPlaza <= 0}
	 * @throws IllegalArgumentException si no existe un aparcamiento con ese id
	 * @throws IllegalArgumentException si no existe una plaza con ese id en el aparcamiento
	 *                                  (excepción propagada de {@link EcoRiderParking#marcarPlazaReparada(int)})
	 */
	public void repararPlazaEnParking(int idParking, int idPlaza) {
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		if(idPlaza < 0)
			throw new IllegalArgumentException(ID_PLAZA_MENOR_QUE_CERO);
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				p.marcarPlazaReparada(idPlaza);
				return;
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	/**
	 * Establece el numero de plazas ampliado despues de cada ampliacion, y añade a la lista de plazas
	 * de cada aparcamiento el numero de plazas correspondiente tras cada ampliacion
	 * 
	 * @param idParking identificador del aparcamiento (mayor que 0)
	 * @throws IllegalArgumentException si {@code idParking <= 0}
	 * @throws IllegalArgumentException si no existe un aparcamiento con ese id
	 * 
	 */
	public void ampliarAparcamiento(int idParking) {
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				p.ampliarAparcamiento();
				return;
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
		
	}
	/**
	 * Establece el numero de plazas con cargador rapido o semirrapido
	 *  ampliado despues de cada ampliacion, y añade a la lista de plazas
	 * de cada aparcamiento el numero de plazas con cargador rapido o semirrapido
	 * correspondiente tras cada ampliacion
	 * @param idParking int id del parking que se va a ampliar 
	 * @param numCargadorRapido int numero de plazas que cuentan con un cargadorRapido
	 * @param numCargadorSemiRapido int numero de plazas que cuentan con un cargadorSemiRapido
	 * @throws IllegalArgumentException si el id del parking es negativo
	 */
	public void ampliarAparcamientoConCargadores(int idParking, int numCargadorRapido, int numCargadorSemiRapido) {
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				p.ampliarAparcamientoConCargador(numCargadorRapido, numCargadorSemiRapido);
				return;
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
		
	}
	
	/**
	 * Permite conocer si una plaza de un parking especifico esta libre
	 * @param idParking int id del parking en el que se encuentra la plaza a comprobar
	 * @param idPlaza int id de la plaza que se quiere comprobar si esta libre
	 * @return {@code true si esta libre}, {@code false si no esta libre}
	 * @throws IllegalArgumentException si el id del parking es menor que cero
	 * @throws IllegalArgumentException si el id de la plaza es menor que cero
	 * @throws IllegalArgumentException si no existe un parking con ese id
	 * @throws IllegalArgumentException excepcion propagada si no existe una plaza con ese id
	 */
	public boolean estaLibreenParking(int idParking, int idPlaza) {
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		if(idPlaza < 0)
			throw new IllegalArgumentException(ID_PLAZA_MENOR_QUE_CERO);
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				return p.estaLibre(idPlaza);
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	/**
	 * Permite conocer si es comptaible un vehiculo con una plaza de un parking especifico
	 * comprobando si es compatible con dicha plaza segun caracteristicas del vehiculo y de la plaza
	 * @param idParking int id del parking en el que se encuentra la plaza a comprobar
	 * @param idPlaza int id de la plaza que se quiere comprobar si la plaza es compatible con el vehiculo
	 * @param v vehiculo que se quiere comprobar
	 * @return {@code true si es compatible}, {@code false si no es compatible}
	 * @throws IllegalArgumentException si el id del parking es menor que cero
	 * @throws IllegalArgumentException si el id de la plaza es menor que cero 
	 * @throws IllegalArgumentException si no existe un parking con ese id
	 * @throws IllegalArgumentException excepcion propagada si no existe una plaza con ese id
	 * @throws NullPointerException si el vehiculo que se intenta comprobar es null
	 */
	public boolean admiteVehiculoenParking(int idParking, int idPlaza, Vehiculo v) {
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		if(idPlaza < 0)
			throw new IllegalArgumentException(ID_PLAZA_MENOR_QUE_CERO);
		
		if(v == null)
			throw new NullPointerException("No puede ser nulo el vehiculo");
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				return p.admiteVehiculo(idPlaza, v);	
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	/**
	 * Permite aparcar un vehiculo en la plaza de un parking especifico comprobando si es compatible con la plaza
	 * deja la plaza como ocupada.
	 * @param idParking int id del parking en el que se encuentra la plaza para aparcar
	 * @param idPlaza int id de la plaza que se quiere aparcar si la plaza es compatible con el vehiculo
	 * @param v vehiculo que se quiere aparcar
	 * @param tarjeta la tarjeta de dicho usuario
	 * @param credencial credencial especifica para el uso de la tarjeta
	 * @throws IllegalArgumentException si el id del parking es menor que cero
	 * @throws IllegalArgumentException si el id de la plaza es menor que cero 
	 * @throws IllegalArgumentException si no existe un parking con ese id
	 * @throws IllegalArgumentException excepcion propagada si no existe una plaza con ese id
	 * @throws NullPointerException si el vehiculo que se intenta comprobar es null
	 */
	public void aparcarVehiculoenParking(int idParking, int idPlaza, TarjetaMonedero tarjeta, String credencial, Vehiculo v) {
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		if(idPlaza < 0)
			throw new IllegalArgumentException(ID_PLAZA_MENOR_QUE_CERO);
		
		if(v == null)
			throw new NullPointerException("El vehiculo no puede ser nulo");
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				p.aparcarVehiculo(idPlaza, v);
				recargarSaldoTarjeta(tarjeta, credencial, fianza);
				return;
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	
	/**
	 * Marca la plaza de la cual se ha pasado id como vacia tras alquilar el vehiculo de esa plaza.
	 * 
	 * @param idParking identificador del aparcamiento (mayor que 0)
	 * @param idPlaza identificador de la plaza (mayor que 0) 
	 * @param tarjeta objeto tarjetaMonedero
	 * @param credencialDescontar credencial para poder descontar el salgo de la tarjeta
	 * @param v vehiculo que va a ser alquilado
	 * @throws IllegalArgumentException si {@code idParking <= 0}
	 * @throws IllegalArgumentException si {@code idPlaza <= 0}
	 * @throws IllegalStateException si el valor de la fianza es mayor que el saldo de la tarjeta
	 * @throws IllegalArgumentException si no existe un aparcamiento con ese id
	 * @throws IllegalArgumentException si no existe una plaza con ese id en el aparcamiento
	 */
	public void alquilarVehiculoEnParkingYPlaza(int idParking, int idPlaza, TarjetaMonedero tarjeta, String credencialDescontar, Vehiculo v) {
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		if(idPlaza < 0)
			throw new IllegalArgumentException(ID_PLAZA_MENOR_QUE_CERO);
		
		if(v == null)
			throw new NullPointerException("El vehiculo no puede ser nulo");
		
		if(fianza > tarjeta.getSaldoActual())
			throw new IllegalStateException("No se puede alquilar por falta de saldo");
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				p.retirarVehiculo(idPlaza);
				v.cogerVehiculo();
				descontarSaldoTarjeta(tarjeta, credencialDescontar, fianza);
				v.setPlazaAsignada(p.buscarPlaza(idPlaza));
				return;
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	
	
	/**
	 * Permite conocer el tipo de vehiculo que se encuentra en una plaza especifica de un parking
	 * @param idParking en el que se encuentra la plaza con el vehiculo
	 * @param idPlaza en la que se encuentra el vehiculo a obtener 
	 * @return Tipo de vehiculo que se encuentra en la plaza 
	 * @throws IllegalArgumentException si el id del parking es menor que cero
	 * @throws IllegalArgumentException si el id de la plaza es menor que cero 
	 * @throws IllegalArgumentException si no existe un parking con ese id
	 * @throws IllegalArgumentException excepcion propagada si no existe una plaza con ese id
	 */
	public Vehiculo obtenerVehiculoenParking(int idParking, int idPlaza) {
		
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		if(idPlaza < 0)
			throw new IllegalArgumentException(ID_PLAZA_MENOR_QUE_CERO);
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				return p.obtenerVehiculoPlaza(idPlaza);
				
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	
	/**
	 * Calcula la distancia que recorre un vehiculo durante su alquiler en km 
	 * conociendo el parking de origen y el de destino
	 * @param v vehiculo que es alquilado y recorre dichos km
	 * @param destino donde se va a aparcar el vehiculo
	 * @return distancia recorrida (double)
	 * @throws NullPointerException si el vehiculo es null
	 * @throws NullPointerException si la plaza de destino es null
	 * @throws IllegalStateException si el vehiculo no ha sido alquilado=no esta en marcha
	 * @throws NullPointerException si la plaza origen es null
	 * @throws IllegalStateException si la distancia recorrida es mayor a los km de autonomia
	 */
	public double calcularKmDeViaje(Vehiculo v, Plaza destino) {
		
		if (v == null)
			throw new NullPointerException("Vehículo null");
	    if (destino == null)
	    	throw new NullPointerException("Plaza destino null");
	    if (!v.estaEnMarcha())
	    	throw new IllegalStateException("El vehículo no está en marcha");
	    
	    Plaza origen = v.getPlazaAsignada();
	    if(origen == null)
	    	throw new NullPointerException("El vehiculo no tiene plaza origen");
	    
	    EcoRiderParking pOrigen = buscarParkingDePlaza(origen);
	    EcoRiderParking pDestino = buscarParkingDePlaza(destino);
	    
	    double distancia = pOrigen.conocerDistanciaParking(pDestino);
	    
	    if(distancia > v.kmAutonomia())
	    	throw new IllegalStateException("La distancia no puede ser mayor a los km de autonomia del vehiculo");
	    
	    
	    v.actualizarContadores(distancia);
	    
	    return distancia;
	    
		
	}
	
	/**
	 * Devuelve el valor del pago del alquiler de un vehiculo
	 * @param v vehiculo usado
	 * @param destino plaza de destino
	 * @param idParking id del parking de origen del que se quiere conocer la tasa de alquiler
	 * @return tasaAlquiler + gasto de llenado de deposito
	 */
	public double getPagoAlquiler(Vehiculo v, Plaza destino, int idParking) {
		
		double kmRecorridos = calcularKmDeViaje(v, destino);
		double gasto = v.obtenerCosteLlenado(kmRecorridos);
		double tasaAlquiler = getTasaAlquiler(idParking);
		
		return tasaAlquiler + gasto;
	}
	/**
	 * Devuelve la fianza que se debe alquilar para alquilar el vehiculo
	 * @param v vehiculo que se quiere alquilar
	 * @return fianza a pagar
	 */
	public double getFianzaAPagarVehiculo(Vehiculo v) {
		
		return v.getFianzaAPagar(fianza);
	}
	
	/**
	 * Devuelve los km de autonomia de un vehiculo especifico
	 * @param v vehiculo del que se quieren conocer los kmAutonomia
	 * @return km de autonomia que tiene un vehiculo
	 */
	public double kmAutonomiaVehiculo(Vehiculo v) {
		
		return v.kmAutonomia();
	}
	/**
	 * Devuelve true o false si un vehiculo esta en marcha o no
	 * @param v vehiculo que queremos saber si esta en marcha
	 * @return {@code true si esta en marcha}, {@code false si no esta en marcha}
	 */
	public boolean enMarchaVehiculo(Vehiculo v) {
		
		return v.estaEnMarcha();
	}
	/**
	 * Devuelve la plaza que esta asignada para un vehiculo
	 * @param v vehiculo con plaza asignada
	 * @return plaza con vehiculo asignado
	 */
	public Plaza obtenerPlazaAsignadaAVehiculo(Vehiculo v) {
		
		return v.getPlazaAsignada();
	}
	/**
	 * Obtiene el id de un vehiculo especifico
	 * @param v vehiculo del que se quiere conocer el id
	 * @return id del vehiculo 
	 */
	public int obtenerIdVehiculo(Vehiculo v) {
		
		return v.getId();
	}
	/**
	 * Obtiene el marca de un vehiculo especifico
	 * @param v vehiculo del que se quiere conocer el marca
	 * @return marca del vehiculo 
	 */
	public String getMarcaVehiculo(Vehiculo v) {
		
		return v.getMarca();
	}
	/**
	 * Obtiene el modelo de un vehiculo especifico
	 * @param v vehiculo del que se quiere conocer el modelo
	 * @return modelo del vehiculo 
	 */
	public String getModelo(Vehiculo v) {
		
		return v.getModelo();
	}
	/**
	 * Obtiene la matricula de un vehiculo especifico
	 * @param v vehiculo del que se quiere conocer la matricula
	 * @return matricula del vehiculo 
	 */
	public String getMatricula(Vehiculo v) {
		
		return v.getMatricula();
	}
	/**
	 * Obtiene los km realizados por un vehiculo especifico
	 * @param v vehiculo del que se quiere conocer el kmRealizado
	 * @param destino plaza de destino
	 * @return numero de kilometros realizados
	 */
	public double getKmRealizados(Vehiculo v, Plaza destino) {
		calcularKmDeViaje(v, destino);
		return v.getKmrealizados();
	}
	/**
	 * 
	 * Obtiene los km totales realizados por un vehiculo especifico
	 * @param v vehiculo del que se quiere conocer el kmTotales
	 * @return numero de kilometros totales
	 */
	public double getKmTotales(Vehiculo v) {
		return v.getKmTotales();
	}
	
	/**
	 * Permite conocer si un parking tiene Cargador o no
	 * @param idParking int id del parking en el que se quiere comprobar el cargador
	 * @param idPlaza int id de la plaza en la que se quiere comprobar el cargador
	 * @return {@code true si la plaza cuenta con cargador}, {@code false si no tiene}
	 * @throws IllegalArgumentException si el id del parking es menor que cero
	 * @throws IllegalArgumentException si el id de la plaza es menor que cero 
	 * @throws IllegalArgumentException si no existe un parking con ese id
	 * @throws IllegalArgumentException excepcion propagada si no existe una plaza con ese id
	 */
	public boolean tieneCargadorenParking(int idParking, int idPlaza) {
		
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		if(idPlaza < 0)
			throw new IllegalArgumentException(ID_PLAZA_MENOR_QUE_CERO);
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				return p.tieneCargador(idPlaza);
				
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	/**
	 * Devuelve el cargador de la plaza de un parking 
	 * @param idParking int id del parking en el que se quiere comprobar el cargador
	 * @param idPlaza int id de la plaza en la que se quiere comprobar el cargador
	 * @return Cargador de la plaza 
	 * @throws IllegalArgumentException si el id del parking es menor que cero
	 * @throws IllegalArgumentException si el id de la plaza es menor que cero 
	 * @throws IllegalArgumentException si no existe un parking con ese id
	 * @throws IllegalArgumentException excepcion propagada si no existe una plaza con ese id
	 */
	public Cargador obtenerCargadorenParking(int idParking, int idPlaza) {
		
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		
		if(idPlaza < 0)
			throw new IllegalArgumentException(ID_PLAZA_MENOR_QUE_CERO);
		
		for(EcoRiderParking p: parkings) {
			if(p.getId() == idParking) {
				return p.getCargador(idPlaza);
				
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	
	/**
	 * Permite encontrar el parking al que pertenece una plaza
	 * @param plaza de donde se busca el parking
	 * @return parking al que pertenece la plaza
	 * @throws IllegalArgumentException si la plaza no exiiste
	 * @throws NullPointerException si la plaza es null
	 */
	public EcoRiderParking buscarParkingDePlaza(Plaza plaza) {
	    if (plaza == null)
	    	throw new NullPointerException("Plaza no puede ser nula");
	    for (EcoRiderParking p : parkings) {
	        for (Plaza pl : p.getPlazas()) {
	            if (pl == plaza) { 
	                return p;
	            }
	        }
	    }
	    throw new IllegalArgumentException("La plaza no existe");
	}

	
	/**
     * Devuelve una copia defensiva de la lista de aparcamientos.
     *
     * @return copia de los aparcamientos registrados
     */
	public List<EcoRiderParking> getParkings() {
		return new ArrayList<>(parkings);
	}
	/**
     * Devuelve el número de aparcamientos registrados.
     *
     * @return número de aparcamientos
     */
	public int getNumParkings() {
		return this.numParkings;
	}
	/**
     * Devuelve los aparcamientos completamente operativos
     * (sin ninguna plaza inoperativa).
     *
     * @return lista de aparcamientos operativos
     */
	public List<EcoRiderParking> getParkingOperativos(){
		ArrayList<EcoRiderParking> parkingsOperativos = new ArrayList<>();
		for(EcoRiderParking parking : parkings) {
			if(parking.getNumInoperativas()==0)
				parkingsOperativos.add(parking);
		}
		return parkingsOperativos;
	}
	/**
     * Devuelve los aparcamientos completos (sin plazas disponibles).
     *
     * @return lista de aparcamientos completos
     */
	public List<EcoRiderParking> getParkingCompletos(){
		ArrayList<EcoRiderParking> parkingsCompletos = new ArrayList<>();
		for(EcoRiderParking parking : parkings) {
			if(parking.getNumDisponibles()==0)
				parkingsCompletos.add(parking);
		}
		return parkingsCompletos;
	}
	/**
     * Devuelve los aparcamientos con plazas disponibles.
     *
     * @return lista de aparcamientos con disponibilidad
     */
	public List<EcoRiderParking> getParkingDisponibles(){
		ArrayList<EcoRiderParking> parkingsDisponibles = new ArrayList<>();
		for(EcoRiderParking parking : parkings) {
			if(parking.getNumDisponibles()>0)
				parkingsDisponibles.add(parking);
		}
		return parkingsDisponibles;
	}
	/**
     * Devuelve los aparcamientos que tienen alguna plaza inoperativa
     * (pero no todas).
     *
     * @return lista de aparcamientos con alguna plaza inoperativa
     */
	public List<EcoRiderParking> getParkingsConAlgunaPlazaInoperativa(){
		ArrayList<EcoRiderParking> parkingsInoperativos = new ArrayList<>();
		for(EcoRiderParking parking : parkings) {
			if(parking.getNumInoperativas()> 0 && parking.getNumInoperativas()< parking.getNumPlazas())
				parkingsInoperativos.add(parking);
		}
		return parkingsInoperativos;
	}
	/**
     * Devuelve los aparcamientos totalmente inoperativos
     * (todas sus plazas inoperativas).
     *
     * @return lista de aparcamientos inoperativos
     */
	public List<EcoRiderParking> getParkingInoperativos(){
		ArrayList<EcoRiderParking> parkingsInoperativos = new ArrayList<>();
		for(EcoRiderParking parking : parkings) {
			if(parking.getNumInoperativas()== parking.getNumPlazas())
				parkingsInoperativos.add(parking);
		}
		return parkingsInoperativos;
	}
	/**
     * Devuelve los aparcamientos más cercanos a una ubicación dentro del
     * radio.
     *
     * @param ubicacion coordenadas de referencia (no nulas)
     * @param radio mayor que cero
     * @return lista de aparcamientos situados a una distancia estrictamente menor que el radio
     * @throws NullPointerException si {@code ubicacion == null}
     */
	public List<EcoRiderParking> getParkingCercanosAUnaUbicacion(CoordenadasGPS ubicacion, double radio){
		
		if(ubicacion == null)
			throw new NullPointerException("La ubicacion no puede ser nula");
		
		if(radio < 0)
			throw new IllegalArgumentException("El radio no puede ser menor que 0");
		
		ArrayList<EcoRiderParking> parkingsCercanos = new ArrayList<>();
		for(EcoRiderParking parking : parkings) {
			if(parking.conocerDistancia(ubicacion) < radio)
				parkingsCercanos.add(parking);
		}
		return parkingsCercanos;
	}
	/**
     * Devuelve los aparcamientos cercanos a otro aparcamiento, usando el radio.
     *
     * @param otroParking aparcamiento de referencia (no nulo)
     * @param radio mayor que cero
     * @return lista de aparcamientos situados a una distancia estrictamente menor que el radio
     * @throws NullPointerException si {@code otroParking == null}
     */
	public List<EcoRiderParking> getParkingCercanosAOtroParking(EcoRiderParking otroParking, double radio){
		
		if(otroParking == null)
			throw new NullPointerException("El parking a comparar no puede ser nulo");
		
		if(radio < 0)
			throw new IllegalArgumentException("El radio no puede ser menor que 0");
		
		ArrayList<EcoRiderParking> parkingsCercanos = new ArrayList<>();
		for(EcoRiderParking parking : parkings) {
			if(parking.conocerDistanciaParking(otroParking) < radio)
				parkingsCercanos.add(parking);
		}
		return parkingsCercanos;
	}
	/**
	 * Devuelve el numero de vehiculos que hay en EcoRiderCity
	 * @return el numero de vehiculos que hay en EcoRiderCity
	 */
	public int getNumVehiculos() {
		return vehiculos.size();
	}
	/**
	 * Devuelve la tasa de alquiler que hay establecida en un parking
	 * @param idParking que se quiere conocer la tasa
	 * @return tasa de alquiler de dicho parking
	 */
	public double getTasaAlquiler(int idParking) {
		
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		for(EcoRiderParking p:parkings) {
			if(p.getId() == idParking) {
				return p.getTasaAlquiler();
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	/**
	 * Devuelve el tamano de bloque de un parking especifico
	 * @param idParking del que se quiere conocer el tamano de bloque
	 * @return tamano de bloque 
	 */
	public int getTamannoBloque(int idParking) {
		
		if(idParking < 0)
			throw new IllegalArgumentException(ID_PARKING_MENOR_QUE_CERO);
		for(EcoRiderParking p:parkings) {
			if(p.getId() == idParking) {
				return p.getTamannoBloque();
			}
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	
	/**
     * Obtiene un aparcamiento por su id.
     *
     * @param id identificador del aparcamiento
     * @return el aparcamiento con ese id
     * @throws IllegalArgumentException si no existe aparcamiento con ese id
     */
	public EcoRiderParking obtenerParkingPorIndice(int id) {
		for(EcoRiderParking parking : parkings) {
			if(parking.getId() == id)
				return parking;
		}
		throw new IllegalArgumentException(NO_EXISTE_PARKING);
	}
	/**
     * Devuelve el aparcamiento situado en la posición {@code i} de la lista interna.
     *
     * @param i índice en la lista (0..size-1)
     * @return aparcamiento en la posición dada
     * @throws IllegalArgumentException si el índice no existe
     */
	public EcoRiderParking obtenerParkingPosicionI(int i) {
		if (i < 0 || i >= parkings.size()) {
	        throw new IllegalArgumentException("Índice fuera de rango: " + i);
	    }
	    return parkings.get(i);
	}
	/**
     * Valida la credencial de cliente para crear/recargar tarjetas.
     *
     * @param credencial credencial a validar
     * @throws IllegalArgumentException si la credencial no coincide con la permitida
     */
	public void validarCredencialInicioTarjeta(String credencial) {
		if(!credencial.equals("A156Bv09_1zXo894"))
			throw new IllegalArgumentException("La credencial que aporta el código cliente no es correcta.");
	}
	/**
     * Devuelve la fianza configurada.
     *
     * @return fianza en la ciudad
     */
	public double getFianza() {
		return this.fianza;
	}
	
	@Override
	public String toString() {
		return "EcoRiderCity [parkings=" + parkings + ", fianza=" + fianza 
				+ ", numParkings=" + numParkings + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EcoRiderCity other = (EcoRiderCity) obj;
		if (Double.doubleToLongBits(fianza) != Double.doubleToLongBits(other.fianza))
			return false;
		if (numParkings != other.numParkings)
			return false;
		if (!parkings.equals(other.parkings))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
	    int result = 1;

	    long temp = Double.doubleToLongBits(fianza);
	    result = 31 * result + (int) (temp ^ (temp >>> 32));


	    result = 31 * result + numParkings;
	    result = 31 * result + (parkings == null ? 0 : parkings.hashCode());

	    return result;
	}
	private EcoRiderCity() {
	    this.parkings = new ArrayList<>();
	    this.vehiculos = new ArrayList<>();
	}

	protected EcoRiderCity clonDelObjeto() {
	    return new EcoRiderCity();
	}

	protected void despuesDeLaCopia(EcoRiderCity src) {
	}

	protected final void hacerElClon(EcoRiderCity src) {
	    this.fianza = src.fianza;
	    this.numParkings = src.numParkings;

	    this.parkings = new ArrayList<>();
	    this.vehiculos = new ArrayList<>();
	}

	@Override
	public final EcoRiderCity clone() {
	    EcoRiderCity c = clonDelObjeto();
	    c.hacerElClon(this);
	    c.despuesDeLaCopia(this);
	    return c;
	}

	
}
