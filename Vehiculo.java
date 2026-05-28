package uva.poo.practica2;

/**
 * Clase abstracta la cual representa los vehiculos del EcoRiderCity.
 * <p>
 * Cada vehiculo tiene un id, marca, modelo, matriculo, kmRealizados,
 * kmTotales, si esta en marcha y una plaza.
 * <p>
 * 
 * @author alvgome
 * @author quegome
 */
public abstract class Vehiculo {

	private int id;
	private String marca;
	private String modelo;
	private String matricula;
	private double kmRealizados;
	private double kmTotales;
	private boolean enMarcha;
	private Plaza plazaAsignada;
		
	/**
	 * Crea un nuevo vehiculo con un identificador, una marca, un modelo y una matricula especificas,
	 * No se usan setter en id marca o modelo ya que no son modificables, pero en matricula si ya que
	 * esta se puede cambiar.
	 * Los contadores de los km se inicializan en 0 y siempre en parado
	 * 
	 * @param marca, representa la marca que es el vehiculo
	 * @param modelo, representa el modelo de el vehiculo
	 * @param matricula, representa el numero de matricula del vehiculo, su longitud no puede ser distinta de 7
	 * 
	 * @throws IllegalArgumentException, si el id es menor que 0 o el modelo y marca se pasan como vacias
	 */
	protected Vehiculo(String marca, String modelo, String matricula) {
		
		
		if(marca.isEmpty())
			throw new IllegalArgumentException("Tienes que introducir una marca");
		this.marca = marca;
		
		if(modelo.isEmpty())
			throw new IllegalArgumentException("Tienes que introducir un modelo");
		this.modelo = modelo;
		
		setMatricula(matricula);
		this.kmRealizados = 0;
		this.kmTotales = 0;
		this.enMarcha = false;
	}
	
	/**
	 * Devuelve los km de autonomia de un vehiculo, cada vehiculo depende de la capacidad
	 * de deposito y el consumo medio
	 * @return el calculo de los kmAutonomia que tiene un vehiculo
	 * 
	 */
	public abstract double kmAutonomia();
	
	/**
	 * Devuelve la fianza a pagar de cada vehiculo
	 * 
	 * @param fianza, la fianza establecida por cada ciudad
	 * @return kilometros de autonomia del vehiculo
	 */
	public abstract double getFianzaAPagar(double fianza);
	
	/**
	 * Devuelve si una plaza es compatible con el vehiculo o no
	 * 
	 * @param plaza, donde se quiere aparcar el vehiculo
	 * @return {@code true si es compatible}, {@code false si no es compatible}
	 */
	public abstract boolean plazaCompatible(Plaza plaza);
	
	/**
	 * Obtener el coste de llenado que se tiene que pagar en el alquiler del vehiculo
	 * 
	 * @param kmRecorridos, en ese tiempo de alquiler, dependiendo de estos se pagará mas o menos
	 * @return coste de llenao del deposito
	 */
	public abstract double obtenerCosteLlenado(double kmRecorridos);
	
	/**
	 * Establecer el numero de ocupantes del vehiculo
	 * @param numOcupantes permite elegir el numero de ocupantes de un vehiculo
	 */
	public abstract void setNumOcupantes(int numOcupantes);
	
	/**
	 * Devuelve el numero de Ocuopantes del vehiculo
	 * @return nuemero de ocupantes del vehiculo
	 */
	public abstract int getNumOcupantes();
	
	/**
     * Establece el consumo medio del vehículo.
     *
     * @param consumoMedio consumo medio del vehículo
     * @throws IllegalArgumentException si consumoMedio es menor que 0.
     */
	public abstract void setConsumoMedio(double consumoMedio);
	
	/**
	 * Devuelve el consumo medio del vehiculo.
	 * @return consumo medio del vehiculo
	 */
	public abstract double getConsumoMedio();
	
	/**
	 * Devuelve la capacidad de bateria del vehiculo
	 * @return capacidad de la bateria del vehiculo
	 */
	public abstract double getCapacidadBateria();
	
	/**
	 * Actualiza los contadores de kmRealizados y kmTotales cada vez que se desplaza un vehiculo
	 * 
	 * @param distancia, distancia recorrida en esa ultima carrera del vehiculo
	 * @throws IllegalArgumentException si la distancia de la carreraes menor que 0  
	 */
	public void actualizarContadores(double distancia) {
		if(distancia < 0)
			throw new IllegalArgumentException("La distancia no puede ser negativa");
		
		this.kmRealizados = distancia;
		this.kmTotales += distancia;
	}
	
	/**
	 * Pone en marcha al vehiculo, inicializa los kmRealizados a 0 y retira el vehiculo de la 
	 * plaza asignada
	 * 
	 * @throws IllegalStateException si llamas al método y el vehiculo esta en marcha
	 */
	public void cogerVehiculo() {
		if(enMarcha)
			throw new IllegalStateException("No se puede coger un vehiculo que esta en marcha");
		
        if (this.plazaAsignada != null) {
            this.plazaAsignada.retirarVehiculoDePlaza();
        }
		
		enMarcha = true;
		this.kmRealizados = 0;
	}
	
	/**
	 * Deja el vehiculo en la plaza
	 * 
	 * @param plaza, donde se dejara el vehiculo
	 * @throws IllegalStateException si el vehiculo no esta en marcha y si el vehiculo no puede aparcar en
	 * esa plaza.
	 * @throws IllegalArgumentException si la plaza no es compatible con el vehiculo
	 */
	public void dejarVehiculo(Plaza plaza) {
		if(!enMarcha)
			throw new IllegalStateException("No se puede dejar un vehiculo que no esta en Marcha");
		
		if (!plazaCompatible(plaza)) {
            throw new IllegalArgumentException("La plaza no es compatible con este vehículo");
		}	
        
        try {
            plaza.aparcar(this);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("No se puede aparcar en esta plaza: " + e.getMessage());
        }
        
        if (this.plazaAsignada != null && this.plazaAsignada != plaza) {
            this.plazaAsignada.retirarVehiculoDePlaza();///Esto esta bien? no deberia ser this.plazaAsignada.aparcar(this)
        }
        
        this.plazaAsignada = plaza;
		enMarcha = false;
	}
	
	/**
	 * Devuelve el estado del vehiculo, true si esta en marcha y false si no
	 * 
	 * @return enMarcha
	 */
	public boolean estaEnMarcha() {
		return enMarcha;
	}
	/**
	 * Cambia el valor de si el vehiculo esta en uso o no.
	 * @param marcha a la que se quiere modificar el vehiculo
	 */
	protected void setEnMarcha(boolean marcha) {
		enMarcha = marcha;
	}
	
	/**
	 * Establece la matricula del vehiculo.
	 * 
	 * @param matricula, cadena que se le asignará al vehiculo
	 * @throws IllegalArgumentException Si la cadena es vacia o tiene lomgitud diferente a 7 o 
	 * no tiene el formato de matricula española
	 */
	public void setMatricula(String matricula) {
	    if (matricula == null || matricula.isEmpty()) {
	        throw new IllegalArgumentException("Tienes que introducir una matrícula");
	    }
	    
	    if (matricula.length() != 7) {
	        throw new IllegalArgumentException("La longitud de la matrícula ha de ser 7");
	    }
	    
	    for (int i = 0; i < 4; i++) {
	        if (!Character.isDigit(matricula.charAt(i))) {
	            throw new IllegalArgumentException("Los primeros 4 caracteres deben ser números (posición " + (i+1) + ")");
	        }
	    }
	    
	    for (int i = 4; i < 7; i++) {
	        char c = matricula.charAt(i);
	        if (!Character.isLetter(c)) {
	            throw new IllegalArgumentException("Los últimos 3 caracteres deben ser letras (posición " + (i+1) + ")");
	        }
	        if (!Character.isUpperCase(c)) {
	            throw new IllegalArgumentException("Las letras deben ser mayúsculas (posición " + (i+1) + ")");
	        }
	    }
	    this.matricula = matricula;
	}
	
	/**
	 * Metodo que establece un id para vehiculo
	 * 
	 * @param id, identificador que se establece para cada vehiculo
	 * @throws IllegalArgumentException en caso de que el id sea menor que 0
	 */
	public void setId(int id) {
		if(id < 0 )
			throw new IllegalArgumentException("El id no puede ser menor que 0");
		this.id = id;
	}
	
	/**
	 * Asigna una plaza al vehiculo
	 * 
	 * @param plaza, la plaza que se asignará al vehiculo
	 */
    public void setPlazaAsignada(Plaza plaza) {
        this.plazaAsignada = plaza;
    }
	  
    /**
     * Obtienes la plaza asignada a el vehiculo
     * 
     * @return plazaAsignada
     */
	public Plaza getPlazaAsignada() {
	        return plazaAsignada;
    }
	
	/**
	 * Obtiene el id del vehiculo
	 * 
	 * @return id, identificado del vehiculo en el sistema
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Obtienes la marca del modelo
	 * 
	 * @return marca
	 */
	public String getMarca() {
		return marca;
	}
	
	/**
	 * Obtienes el modelo del vehiculo
	 * 
	 * @return modelo
	 */
	public String getModelo() {
		return modelo;
	}
	
	/**
	 * Obtienes la matricula del vehiculo
	 * 
	 * @return matricula
	 */
	public String getMatricula() {
		return matricula;
	}
	
	/**
	 * Obtienes el numero de km recorridos en la ultima carrera
	 * 
	 * @return kmRealizados
	 */
	public double getKmrealizados() {
		return kmRealizados;
	}
	
	/**
	 * Obtienes el numero de km recorridos totales por el vehiculo
	 * 
	 * @return kmTotales
	 */
	public double getKmTotales() {
		return kmTotales;
	}
	
	protected abstract Vehiculo clonDelObjeto();
	
	protected void despuesDeLaCopia(Vehiculo src) {}
	
	protected final void hacerElClon(Vehiculo src) {
	    this.id = src.id;
	    this.matricula = src.matricula;
	    this.kmRealizados = src.kmRealizados;
	    this.kmTotales = src.kmTotales;
	    this.enMarcha = src.enMarcha;
	    this.plazaAsignada = src.plazaAsignada;
	}
	@Override
	public final Vehiculo clone() {
	    Vehiculo c = clonDelObjeto();   // polimórfico: lo implementa cada subclase
	    c.hacerElClon(this);    		// copia estado común (privados incluidos)
	    c.despuesDeLaCopia(this);       // hook para que subclases copien lo suyo
	    return c;
	}
	


}
