package uva.poo.practica2;

import java.util.ArrayList;
import java.util.List;

import fabricante.externo.tarjetas.TarjetaMonedero;
import uva.poo.practica2.Plaza.EstadoPlaza;

/**
 * Representa cada aparcamiento de la aplicacion de EcoRiderSharing
 * <p>
 * Cada aparcamiento tiene un identificador, un numero de plazas, una ubicacion,<br>
 * un tamanno que sera el numero de plazas que en cada ampliacion podra aumentar el aparcamiento.<br>
 * Y una costante que es un String que se repetia varias veces en el codigo en caso de excepcion.<br>
 * Tambien tiene una tasa de alquiler y una serie de cargadores que la longitud de la lista es menor que lade las plazas.<br> 
 * Esta clase puede modificar el etado de las plazas.<br>
 * Debe tener como minimo estas funcionalidades.
 * </p>
 * <ul>
 * <li>Conocer cantidad de plazas de un aparcamiento.
 * <li>Conocer la cantidad de plazass ocupadas.
 * <li>Conocer cuantas plazas estan disponibles.
 * <li>Conocer cuantas plazas estas inoperativas.
 * <li>Conocer cuantas plazas tienen cargador rapido.
 * <li>Conocer cuantas plazas tienes cargador semirapido.
 * <li>Conocer posicion GPS donde se encuentra el aparcamiento.
 * <li>Conocer si una plaza especifica tiene cargador.
 * <li>Conocer el id del aparcamiento.
 * <li>Sabiendo el id, saber su estado.
 * <li>Alquilar un vehiculo de una plaza.
 * <li>Devolver un vehiculo de una plaza.
 * <li>Conocer la distancia a otro Aparcamiento.
 * <li>Añadir plazas a un aparcamiento segun el numero de bloques.
 * <li>Ampliar el aparcamiento con un numero de plazas rapidas y semirapidas a elegir.
 * <li>Mas las funcionalidades de la clase plaza como por ejemplo marcarPlazaComoInoperativa,
 * estaLibre, tieneCargador, etc.
 * </ul>
 * 
 * Y nosotros hemos añadido alguna funcionalidad como:
 * <ul>
 * <li>Un metodo obtenerPlazaLibre en caso de que la haya devuelve la primera que este vacia.
 * <li>Un metodo marcarComoInoperativa, sabiendo el id marca esa plaza como inoperativa.
 * <li>Un metodo reservarVehiculo, sabiendo el id de esa plaza y comprobando que este ocupado,
 * reserve el vehiculo.
 * <li>Un metodo repararPlaza, dando un id repare esa plaza si esta inoperativa.
 * <li>Un metodo que permita conocerDistancia a cualquier ubicacion no solo a la de otro parking.
 * <li>Metodos que devuelvan el numero de plazas:
 * <li>1.Disponibles.
 * <li>2.Ocupadas.
 * <li>3.Inoperativas.
 * </ul>
 * 
 * @author alvgome
 * @author quegome
 */
public class EcoRiderParking {
	
	private static final String ERRORID = "No existe plaza con ese id";
	
	private int numPlazas;
	private CoordenadasGPS ubicacion;
	private int id;
	private int tamanoBloque;
	private ArrayList<Plaza> plazas;
	ArrayList<Cargador> cargadores;
	private double tasaDeAlquiler;
	
	
	/**
	 * Crea un nuevo aparcamiento con un numero de plazas, una ubicacion, un identificador, un tamaño de bloque, un numero de
	 * cargadores rapido y otro de semirapidos, y una tasa de alquiler.
	 * No se usan setter para estos parametros debido a que una vez establecidos para el parking, no se pueden modificar.
	 * Solo los usamos para la creacion de plazas y cargadores para que quede mas limpio el codigo aunque no seria necesario.
	 * 
	 * @param numPlazas, numero de plazas que tendra el parking, no puede ser menor que 0
	 * @param ubicacion, ubicacion que tendra el parking, no puede tener valor null
	 * @param tamanoBloque, tamaño que determinara el numero de plazas que se añadir en cada aumento del parking,
	 *  tiene que ser mayor que 0.
	 *  @param numCargadorRapido, numero de cargadores rapidos que tendra las plazas de este parking
	 *  @param numCargadorSemiRapido, numero de cargadores semirapidos que tendran las plazas de este parking
	 *  @param tasaDeAlquiler, alquiler que hay que pagar por alquilar de coche segun el parking
	 *  
	 *  @throws IllegalArgumentException si el numero de plazas es menor que 0, si el tamaño del bloque no es mayor que 0,
	 *  si el numero de cargadores rapidos y semirapidos es menor que 0, si la suma de los cargadores es mayor que el 
	 *  numero de plazas y si la tasa de alquiler no es mayor que 0
	 */
	public EcoRiderParking(int numPlazas, int numCargadorRapido, int numCargadorSemiRapido, CoordenadasGPS ubicacion, int tamanoBloque, double tasaDeAlquiler) {
		
		if (tamanoBloque <= 0)
	        throw new IllegalArgumentException("El tamaño de bloque debe ser mayor que cero");

		this.tamanoBloque = tamanoBloque;
		
		if(numPlazas < 0)
			throw new IllegalArgumentException("El numero de plazas de un aparcamiento no puede ser negativo");
		this.numPlazas = numPlazas;
		
		if(numCargadorRapido < 0)
			throw new IllegalArgumentException("El numero de plazas con cargador rapido de un aparcamiento no puede ser negativo");
		
		if(numCargadorSemiRapido < 0)
			throw new IllegalArgumentException("El numero de plazas con cargador semirapido de un aparcamiento no puede ser negativo");
		
		if(numCargadorRapido+numCargadorSemiRapido > numPlazas)
			throw new IllegalArgumentException("El numero de plazas con cargador no puede ser mayor que el numero de plazas");
		
		if(tasaDeAlquiler <= 0)
			throw new IllegalArgumentException("La tasa de alquiler debe ser mayor que 0");
		
		setUbicacion(ubicacion);
		
		int numCargador=numCargadorRapido+numCargadorSemiRapido;
		
		this.plazas = new ArrayList<>();
		this.cargadores = new ArrayList<>();
		
		setCargadores(numCargadorRapido, numCargadorSemiRapido);
		
		setPlazas(numPlazas, numCargador, cargadores);
		
		this.tasaDeAlquiler = tasaDeAlquiler;
		
	}
	
	/**
	 * Metodo que crea las plazas segun los cargadores que haya, las primeras plazas seran con cargador,
	 * y las ultimas seran sin cargador.
	 * 
	 * @param numPlazas, numero de plazas que hay en el parking
	 * @param numCargador, numero de cargadores conllevará la creación de plazas con cargadores,
	 * tambien se puede tener con cargadores.size()
	 * @param cargadores, lista de cargadores que hay que pueden ser rapidos o semirrapidos.
	 */
	public void setPlazas(int numPlazas, int numCargador, ArrayList<Cargador> cargadores) {
		for(int i = 0; i < numCargador; i++) {
			plazas.add(new PlazaConCargador(i, cargadores.get(i)));
		}
		for(int j = numCargador; j < numPlazas; j++) {
			plazas.add(new PlazaSinCargador(j));
		}
	}
	
	/**
	 * Metodo que crea la lista de cargadores segun sean rapidos o semirrapidos,
	 * primeros seran cargadores rapidos y los ultimos semirapidos
	 * 
	 * @param numCargadorRapido, numero de cargadores rapidos del parking
	 * @param numCargadorSemiRapido, numero de cargadores semirapidos del parking
	 */
	public void setCargadores(int numCargadorRapido, int numCargadorSemiRapido){
		for(int i = 0; i <numCargadorRapido; i++) {
			cargadores.add(new Cargador(i, 31.2, 5.3, TipoCorriente.CONTINUA));
		}
		for(int j = numCargadorRapido; j<numCargadorSemiRapido+numCargadorRapido; j++) {
			cargadores.add(new Cargador(j, 12.9, 3.4, TipoCorriente.ALTERNA));
		}
	}
	
	
		
	
	/**
	 * Recorre la lista de plazas y devuelve en el caso de que existan la primera vacia,
	 * en el caso de que no haya ninguna vacia devuelve una excepcion.
	 * 
	 * @return p, en caso de que haya plazas libres devolverá la primera de la lista.
	 * @throws IllegalStateException en caso de que no haya plazas libres.
	 */
	public Plaza obtenerPlazaLibre() {
		for( Plaza p : plazas) {
			if(p.getEstado() ==  Plaza.EstadoPlaza.VACIA) {
				return p;
			}
		}
		throw new IllegalStateException("No hay plazas libres"); 
	}
	
	/**
	 * Devuelve el estado de una plaza de la cual se le ha pasado un identificador, sino existe plaza
	 * con ese identificador devuelve una excepcion
	 * 
	 * @param id, identificador de la plaza que estamos buscando.
	 * @return p.gestEstado(), en caso de que exista plaza con ese id se devuelve su estado.
	 * @throws IllegalArgumentException en el caso de que no exista plaza con ese id.
	 */
	public EstadoPlaza conocerEstado(int id) {
		
		return buscarPlaza(id).getEstado();
	}
	
	/**
	 * Busca la plaza por id y devuelve si esta esta libre es decir sin vehiculo y vacia o no
	 * 
	 * @param id, identificador de la plaza
	 * @return true en el caso de que este libre, false en el caso de que no.
	 */
	public boolean estaLibre(int id) {
		
		return buscarPlaza(id).estaLibre();
	}
	
	/**
	 * Comprueba si la plaza con ese id del parking, admite el vehiculo dado
	 * 
	 * @param id, identificador de la plaza
	 * @param v, vehiculo que se le pasa al metodo para ver si lo admite
	 * @return true en el caso de que sean compatibles, false en el caso de que  no
	 */
	public boolean admiteVehiculo(int id, Vehiculo v) {
		
		return buscarPlaza(id).admite(v);
	}
	
	/**
	 * Aparca el vehiculo en la plaza de la cual se ha pasado el id
	 *  
	 * @param id, identificador de la plaza
	 * @param v, vehiculo que se le pasa al metodo para aparcarlo en la plaza
	 */
	public void aparcarVehiculo(int id, Vehiculo v) {
		
		buscarPlaza(id).aparcar(v);
	}
	
	/**
	 * Retira el vehiculo de la plaza
	 * 
	 * @param id, identificador de la plaza donde se llamara al metodo retirar Vehiculo
	 */
	public void retirarVehiculo(int id) {
		
		buscarPlaza(id).retirarVehiculoDePlaza();
	}
	
	/**
	 * Obtienes el vehiculo de la plaza con el id dado
	 * 
	 * @param id, identificador de la plaza
	 * @return Vehiculo que se encuentre asociado a la plaza dada
	 */
	public Vehiculo obtenerVehiculoPlaza(int id) {
		
		return buscarPlaza(id).getVehiculo();
	}
	
	/**
	 * Establece el vehiculo v en la plaza con id dado
	 * 
	 * @param id, identificador de la plaza donde se establecerá el vehiculo
	 * @param v, vehiculo dado que se asignará a la plaza
	 */
	public void setVehiculoPlaza(int id, Vehiculo v) {
		
		buscarPlaza(id).setVehiculo(v);
	}
	
	/**
	 * Comprueba si la plaza con ese identificador tiene cargador o no
	 * 
	 * @param id, identificador de la plaza
	 * @return true si tiene cargador, false sino
	 */
	public boolean tieneCargador(int id) {
		
		return buscarPlaza(id).tieneCargador();
	}
	
	/**
	 * Obtienes el cargador de la plaza con el identificador dado
	 * 
	 * @param id, identificador de la plaza buscada
	 * @return Cargador de la plaza
	 */
	public Cargador getCargador(int id) {
		
		return buscarPlaza(id).getCargador();
	}
	
	/**
	 * Marca la plaza de la cual se ha pasado id como inoperativa, en el caso 
	 * de que no exista plaza con ese id devolvera una excepcion
	 * 
	 * @param id, identificador de la plaza deseada
	 * @throws IllegalArgumentException en el caso de que no exista plaza con ese id.
	 */
	public void marcarPlazaInoperativa(int id) {
		
		buscarPlaza(id).marcarComoInoperativa();
	}
	
	/**
	 * Marca la plaza de la cual se ha pasado id como vacia tras alquilar el vehiculo de esa plaza,
	 * en el caso de que no exista plaza con ese id devolvera una excepcion
	 * 
	 * @param id, identificador de la plaza deseada
	 * @throws IllegalArgumentException en el caso de que no exista plaza con ese id.
	 */
	public void marcarPlazaComoVacia(int id) {
		
		buscarPlaza(id).marcarPlazaVacia();
	}
	
	/**
	 * Marca la plaza de la cual se ha pasado id como ocupada tras dejar el vehiculo en esa plaza,
	 * en el caso de que no exista plaza con ese id devolvera una excepcion
	 * 
	 * @param id, identificador de la plaza deseada
	 * @throws IllegalArgumentException en el caso de que no exista plaza con ese id.
	 */
	public void marcarPlazaComoOcupada(int id) {
		
		buscarPlaza(id).marcarPlazaOcupada();
	}
	
	/**
	 * Marca la plaza de la cual se ha pasado id como reservada, en el caso 
	 * de que no exista plaza con ese id devolvera una excepcion
	 * 
	 * @param id, identificador de la plaza deseada
	 * @throws IllegalArgumentException en el caso de que no exista plaza con ese id.
	 */
	public void reservarVehiculo(int id) {
	
		buscarPlaza(id).marcarPlazaReservada();
	}
	
	/**
	 * Marca la plaza de la cual se ha pasado id como reparada en el caso de que dicha plaza estuviera como inoperativa,
	 * en el caso de que no exista plaza con ese id devolvera una excepcion
	 * 
	 * @param id, identificador de la plaza deseada
	 * @throws IllegalArgumentException en el caso de que no exista plaza con ese id.
	 */
	public void marcarPlazaReparada(int id) {
	   
	    buscarPlaza(id).repararPlaza(); 
	}
	
	/**
	 * Metodo auxiliar para no repetir todo el rato el mismo codigo al cual se le pasa un id
	 * y busca en el parking una plaza con ese id y la devuelve
	 * 
	 * @param id, identificador de la plaza que se esta buscando
	 * @return p plaza con el identificador dada
	 * @throws IllegalArgumentException en el caso de que no exista una plaza en el parking con el identificador dado
	 */
	public Plaza buscarPlaza(int id) {
		for(Plaza p:plazas) {
			if(p.getID() == id)
				return p;
		}
		throw new IllegalArgumentException(ERRORID);
	}
	
	/**
	 * Devuelve la distancia que hay desde el parking a una ubicacion que se le pasa al metodo,
	 * si la ubicacion que se le pasa es nula devolvera una excepcion
	 * 
	 * @param otraUbicacion, ubicacion con la que calcularemos la distancia a nuestro parking
	 * @return la distancia entre el parking y la ubicacion dada
	 * @throws NullPointerException en caso de que la ubicacion sea null devolvera la excepcion
	 */
	public double conocerDistancia(CoordenadasGPS otraUbicacion) {
		if(otraUbicacion == null)
			throw new NullPointerException("La nueva ubicacion no puede ser null");
		return ubicacion.calcularDistancia(otraUbicacion);
	}
	
	/**
	 * Devuelve la distancia que hay desde el parking a otro parking el cual se pasa como argumento,
	 * si el parking que se le pasa es nulo devolvera una excepcion
	 * 
	 * @param otroParking, parking con el que calcularemos  la distancia a nuestro parking
	 * @return la distancia entre el parking y el otro parking
	 * @throws NullPointerException en caso de que el parking sea null devolvera la excepcion
	 */
	public double conocerDistanciaParking(EcoRiderParking otroParking) {
		if(otroParking == null)
			throw new NullPointerException("No puede ser nulo el otro parking");
		return ubicacion.calcularDistancia(otroParking.getUbicacion());
	}
	
	/**
	 * Establece el numero de plazas ampliado despues de cada ampliacion, y añade a la lista de plazas
	 * de cada aparcamiento el numero de plazas correspondiente tras cada ampliacion sin cargador
	 */
	public void ampliarAparcamiento() {
		int iniciales = getNumPlazas();
		for(int i=0; i < tamanoBloque; i++) {
			plazas.add(new PlazaSinCargador(iniciales + i));
		}
		this.numPlazas = plazas.size();
	}
	
	/**
	 * Amplia el aparcamiento con plazas con cargador y plazas sin cargador.
	 * Primero en el caso de que el numero de cargadores sea menor que el tamaño del bloque
	 * aumentaras con plazas sin cargador hasta el resto de tamaño bloque menos el numero de cargadores
	 * y luego a partir de esas plazas crearias plazas con cargadores a través de la lista de los cargadores
	 * ampliada.
	 * Luego guardas el numero de plazas
	 * 
	 * @param numCargadorRapido, numero de plazas con cargadores rapidos que se amplian
	 * @param numCargadorSemiRapido, numero de plazas con cargadores semirapidos que se amplian.
	 */
	public void ampliarAparcamientoConCargador(int numCargadorRapido, int numCargadorSemiRapido) {

		int iniciales = getNumPlazas();
		int cargadoresIniciales = getNumCargadores();
		ampliarCargadores(numCargadorRapido, numCargadorSemiRapido);
		int resto = tamanoBloque - (numCargadorRapido+numCargadorSemiRapido);
		
		for(int i=0; i < resto; i++) {
			plazas.add(new PlazaSinCargador(iniciales + i));
		}
		
		for(int i=0; i < tamanoBloque-resto; i++) {
			plazas.add(new PlazaConCargador(iniciales+ resto + i, cargadores.get(cargadoresIniciales+i)));
		}
		this.numPlazas = plazas.size();
	}
	
	/**
	 * Amplia la lista de cargadores siempre que la suma de los parametros numero de  cargadores rapidos y semirapidos, 
	 * no sea mayor a el tamaño del bloque aumenta primero en la lista de cargadores con el numero de cargadores rapidos, 
	 * luego con el numero de cargadores semirapidos
	 * 
	 * @param numCargadorRapido, numero de cargadores rapidos que se aumentaran en la lista
	 * @param numCargadorSemiRapido, numero de cargadores semirapidos que se aumentaran en la lista.
	 * 
	 * @throws IllegalArgumentException en el caso de que la suma de los cargadores sea mayor que el tamaño del bloque.
	 */
	public void ampliarCargadores(int numCargadorRapido, int numCargadorSemiRapido) {
		if (numCargadorRapido+numCargadorSemiRapido > tamanoBloque)
			throw new IllegalArgumentException("La suma de las plazas con cargadores tiene que ser menor o igual al tamaño de bloque");
		
		int iniciales = getNumCargadores();
		for(int i = 0; i < numCargadorRapido; i++) {
			cargadores.add(new Cargador(iniciales + i, 31.2, 5.3, TipoCorriente.CONTINUA ));
		}
		
		for(int j = numCargadorRapido; j < (numCargadorRapido+numCargadorSemiRapido); j++) {
			cargadores.add(new Cargador(iniciales + j, 12.9, 3.4, TipoCorriente.ALTERNA));
		}
	}
	
	/**
	 * Metodo que establece un id para aparcamiento
	 * 
	 * @param id, identificador que se establece para cada aparcamiento
	 * @throws IllegalArgumentException en caso de que el id sea menor que 0
	 */
	public void setId(int id) {
		if(id < 0 )
			throw new IllegalArgumentException("El id no puede ser menor que 0");
		this.id = id;
	}
	
	/**
	 * Establece la ubicacion del parking
	 * 
	 * @param ubicacion establece la ubicacion del parking
	 * @throws NullPointerException lanza una excepcion en caso de que la ubicacion sea null
	 */
	public void setUbicacion(CoordenadasGPS ubicacion) {
		if(ubicacion == null)
			throw new NullPointerException("La ubicacion no puede ser nula");
		this.ubicacion = ubicacion;
	}
	
	/**
	 * Devuelve el numero de plazas del aparcamiento
	 * 
	 * @return numero de plazas que hay
	 */
	public int getNumPlazas() {
		return plazas.size();
	}
	
	/**
	 * Devuelve el numero de plazas del aparcamiento
	 * 
	 * @return numero de cargadores que hay en el parking
	 */
	public int getNumCargadores() {
		return cargadores.size();
	}
	
	/**
	 * Devuelve el numero de plazas ocupadas del aparcamiento
	 * 
	 * @return numero de plazas ocupadas
	 */
	public int getNumOcupadas() {
		int cont = 0;
		for (Plaza p : plazas) {
			if(p.getEstado() == Plaza.EstadoPlaza.OCUPADA)
				cont++;
		}
		return cont;
	}

	/**
	 * Devuelve el numero de plazas disponibles del aparcamiento
	 * 
	 * @return numero de plazas disponibles del aparcamiento
	 */
	public int getNumDisponibles() {
		int cont = 0;
		for ( Plaza p : plazas) {
			if(p.getEstado() == Plaza.EstadoPlaza.VACIA)
				cont++;
		}
		return cont;
	}
	
	/**
	 * Devuelve el numero de plazas inoperativas del aparcamiento
	 * 
	 * @return numero de plazas inoperativas del aparcamiento
	 */
	public int getNumInoperativas() {
		int cont = 0;
		for(Plaza p : plazas) {
			if(p.getEstado() ==  Plaza.EstadoPlaza.INOPERATIVA)
				cont++;
		}
		return cont;
	}
	/**
	 * Devuelve el numero de plazas con cargador rapido
	 * 
	 * @return numero de plazas con cargador rapido
	 */
	public int getNumRapidas() {
		int cont = 0;
		for(Plaza p : plazas) {
			if(p.tieneCargador() && p.getCargador().getCorriente() == TipoCorriente.CONTINUA)
				cont++;
		}
		return cont;
	}
	/**
	 * Devuelve el numero de plazas con cargador semi rapido
	 * 
	 * @return numero de plazas con cargador semi rapido
	 */
	public int getNumSemiRapidas() {
		int cont = 0;
		for (Plaza p : plazas) {
			if(p.tieneCargador() && p.getCargador().getCorriente() == TipoCorriente.ALTERNA)
				cont++;
		}
		return cont;
	}
	
	/**
	 * Devuelve la ubicacion del aparcamiento
	 * 
	 * @return la ubicacion del aparcamineto
	 */
	public CoordenadasGPS getUbicacion() {
		return this.ubicacion;
	}
	
	/**
	 * Devuelve el identificador del aparcamiento
	 * 
	 * @return el id del aparcamiento
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Devuelve una copia de la lista de plazas del aparcamiento.
	 * Devuelve una copia para asi evitar la modificacion directa de la lista interna
	 * 
	 * @return una copia de la lista de plazas del aparcamiento
	 */
	public List<Plaza> getPlazas() {
		return new ArrayList<>(plazas);
	}
	
	/**
	 * Devuelve una copia de la lista de los cargadores del aparcamiento.
	 * Devuelve copia para asi evitar modificacion directa de la lista interna.
	 * 
	 * @return una copia de la lista de los cargadores
	 */
	public List<Cargador> getCargadores() {
		return new ArrayList<>(cargadores);
	}
	
	/**
	 * Devuelve el valor del numero de plazas que se aumente el numero de bloques
	 * 
	 * @return el valor de la variable tamanno del bloque
	 */
	public int getTamannoBloque() {
		return tamanoBloque;
	}
	/**
	 * Devuelve la tasa de alquiler del parking
	 * 
	 * @return tasaDeAqluiler
	 */
	public double getTasaAlquiler() {
		return tasaDeAlquiler;
	}
	@Override
	public String toString() {
		return "EcoRiderParking [numPlazas=" + getNumPlazas() + ", ubicacion=" + ubicacion + ", id=" + id + ", tamanoBloque="
				+ tamanoBloque + ", plazas=" + plazas + "]";
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null || getClass() != obj.getClass())
	        return false;
	    EcoRiderParking other = (EcoRiderParking) obj;
	    // Dos aparcamientos se consideran iguales si tienen el mismo id y ubicación
	    return id == other.id && 
	           (ubicacion != null ? ubicacion.equals(other.ubicacion) : other.ubicacion == null);
	}
	
	@Override
	public int hashCode() {
	    int result = Integer.hashCode(id);
	    result = 31 * result + (ubicacion != null ? ubicacion.hashCode() : 0);
	    return result;
	}
	private EcoRiderParking() {
	    this.plazas = new ArrayList<>();
	    this.cargadores = new ArrayList<>();
	}

	protected EcoRiderParking clonDelObjeto() {
	    return new EcoRiderParking();
	}

	protected void despuesDeLaCopia(EcoRiderParking src) {
	}

	protected final void hacerElClon(EcoRiderParking src) {
	    this.numPlazas = src.numPlazas;          
	    this.ubicacion = src.ubicacion;          
	    this.id = src.id;
	    this.tamanoBloque = src.tamanoBloque;
	    this.tasaDeAlquiler = src.tasaDeAlquiler;

	    this.plazas = new ArrayList<>();
	    this.cargadores = new ArrayList<>();
	}

	@Override
	public final EcoRiderParking clone() {
	    EcoRiderParking c = clonDelObjeto();
	    c.hacerElClon(this);
	    c.despuesDeLaCopia(this);
	    return c;
	}

}
