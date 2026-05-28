package uva.poo.practica2;

/**
 * Clase abstracta que representa cada plaza individual del EcoRiderParking.
 * <p>
 * Cada plaza tiene un identificador único y un estado que puede ser:<br>
 * {@code VACIA}, {@code OCUPADA}, {@code RESERVADA} o {@code INOPERATIVA}.<br>
 * El estado se modifica según las operaciones realizadas sobre la plaza.<br>
 * Permite aparcar y retirar un vehiculo de una plaza.<br>
 * </p>
 * 
 * @author alvgome
 * @author quegome
 */
public abstract class Plaza {
	
	private int id;
	private EstadoPlaza estado;
	private Vehiculo vehiculo;
	
	/**
     * Enumeración que define los posibles estados de una plaza.
     */
	public enum EstadoPlaza {
        VACIA,
        OCUPADA,
        RESERVADA,
        INOPERATIVA
    }
	
	
	//Metodos Abstractos
	public abstract boolean tieneCargador();
	public abstract Cargador getCargador();
	public abstract boolean admite(Vehiculo v);
	
	/**
     * Crea una nueva plaza con el identificador especificado y la inicializa en estado {@code VACIA}.
     *
     * @param id Identificador de la plaza. Debe ser mayor o igual que 0.
     * @throws IllegalArgumentException si el identificador es menor que 0.
    */
	protected Plaza(int id) {
		if ( id < 0 )
			throw new IllegalArgumentException("El ID no puede ser negativo");
		this.id = id;
		this.estado = EstadoPlaza.VACIA;
	}
	
    
	/**
     * Marca la plaza como vacía tras retirar el vehículo.
     *
     * @throws IllegalStateException si la plaza no está en estado {@code OCUPADA}.
     */
	public void marcarPlazaVacia() {
		if(estado == EstadoPlaza.INOPERATIVA)
			throw new IllegalStateException("No puedes alquilar un vehiculo de una plaza que esta inoperativa");
		if(estado == EstadoPlaza.RESERVADA)
			throw new IllegalStateException("No puedes alquilar un vehiculo que ya este reservado");
		if(estado == EstadoPlaza.VACIA)
			throw new IllegalStateException("No puedes alquilar un vehiculo de una plaza vacia");
		estado = EstadoPlaza.VACIA;
	}
	
	/**
     * Ocupa la plaza dejando un vehículo en ella.
     *
     * @throws IllegalStateException si la plaza no está en estado {@code VACIA}.
     */
	public void marcarPlazaOcupada() {
		if(estado == EstadoPlaza.INOPERATIVA)
			throw new IllegalStateException("No puedes dejar un vehiculo en una plaza inoperativa");
		if(estado == EstadoPlaza.OCUPADA)
			throw new IllegalStateException("No puedes dejar el vehiculo en una plaza ocupada");
		if(estado == EstadoPlaza.RESERVADA)
			throw new IllegalStateException("No puedes dejar un Vehiculo en una plaza reservada");			
		estado = EstadoPlaza.OCUPADA;
	}
	

    /**
     * Reserva la plaza, cambiando su estado a {@code RESERVADA}.
     *
     * @throws IllegalStateException si la plaza no está en estado {@code OCUPADA}.
     */
	public void marcarPlazaReservada() {
		if(estado == EstadoPlaza.INOPERATIVA)
			throw new IllegalStateException("No puedes reservar un vehiculo de una plaza que esta inoperativa");
		if(estado == EstadoPlaza.RESERVADA)
			throw new IllegalStateException("No puedes reservar un vehiculo de una plaza que ya este reservada");
		if(estado == EstadoPlaza.VACIA)
			throw new IllegalStateException("No puedes reservar un vehiculo de una plaza vacia");
		estado = EstadoPlaza.RESERVADA;
	}
	
    /**
     * Marca la plaza como inoperativa.
     *
     * @throws IllegalStateException si la plaza ya está en estado {@code INOPERATIVA}.
     */
	public void marcarComoInoperativa() {
		if(estado == EstadoPlaza.INOPERATIVA)
			throw new IllegalStateException("No se puede marcar como inoperativa una plaza ya inoperativa");
		estado = EstadoPlaza.INOPERATIVA;
	}
	

    /**
     * Repara una plaza inoperativa, cambiando su estado a {@code VACIA}.
     *
     * @throws IllegalStateException si la plaza no está en estado {@code INOPERATIVA}.
     */
	public void repararPlaza() {
		if(estado != EstadoPlaza.INOPERATIVA)
			throw new IllegalStateException("Solo se pueden reparar plazas que sean inoperativas");
		estado = EstadoPlaza.VACIA;
	}
	/**
	 * Deja un vehiculo en una plaza, cambiando su estado a {@code OCUPADA} y almacena el vehiculo en this.vehiculo.
	 * 
	 * @param v vehiculo que se va a aparcar
	 * @throws NullPointerException si se aparca un vehiculo null
	 * @throws IllegalStateException si se aparca en una plaza con estado inoperativo
	 * @throws IllegalStateException si se aparca en una plaza ocupada
	 * @throws IllegalStateException si es vehiculo no es es compatible con la plaza
	 */
	public void aparcar(Vehiculo v) {
		if (v == null)
			throw new NullPointerException("vehiculo no puede ser null");
		if (estado == EstadoPlaza.INOPERATIVA)
			throw new IllegalStateException("No se puede aparcar en una plaza inoperativa");
		if (!estaLibre())
			throw new IllegalStateException("No es una plaza libre");
		if (!admite(v)) 
			throw new IllegalStateException("Vehiculo no compatible con la plaza");
		v.setEnMarcha(false);
		this.vehiculo=v;
		marcarPlazaOcupada();
	}
	/**
	 * Retira un vehiculo estacionado en una plaza para su uso, cambia su estado a {@code VACIA} y deja vacia la variable vehiculo.
	 * 
	 * @throws NullPointerException si la plaza esta vacia, es decir, vehiculo=null.
	 */
	public void retirarVehiculoDePlaza() {
		if (vehiculo == null)
			throw new NullPointerException("Vehiculo no puede ser null");
		this.vehiculo = null;
		marcarPlazaVacia();
	}
	
    /**
     * Devuelve el identificador de la plaza.
     *
     * @return Identificador de la plaza.
     */
	public final int getID() {
		return id;
	}
	
    /**
     * Devuelve el estado actual de la plaza.
     *
     * @return Estado de la plaza.
     */
	public final EstadoPlaza getEstado() {
		return estado;
	}
	
	/**
	 * Metodo que comprueba si una plaza esta libre o no,es decir,
	 * si contiene vehiculo o no
	 * 
	 * @return true si esta vacia, false si tiene vehiculo o sino esta vacia
	 */
	public final boolean estaLibre() {
		return vehiculo==null && estado==EstadoPlaza.VACIA;
	}
	/**
	 * Introduce un vehiculo.
	 * @param vehiculo que se va a colocar en una plaza
	 */
	public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
        marcarPlazaOcupada();
    }
    /**
     * Devuelve un vehiculo.
     * @return vehiculo
     */
    public Vehiculo getVehiculo() {
        return vehiculo;
    }
	
	@Override
	public String toString() {
		return "Plaza [id=" + id + ", estado=" + estado + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plaza other = (Plaza) obj;
		if (estado != other.estado)
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public int hashCode() {
	    int result = Integer.hashCode(id);
	    result = 31 * result + (estado != null ? estado.hashCode() : 0);
	    return result;
	}
	protected abstract Plaza clonDelObjeto();
	
	protected void despuesDeLaCopia(Plaza src) {}
	
	protected final void hacerElClon(Plaza src) {
	    this.id = src.id;
	    this.estado = src.estado;
	    this.vehiculo = src.vehiculo;

	}
	@Override
	public final Plaza clone() {
	    Plaza c = clonDelObjeto();   // polimórfico: lo implementa cada subclase
	    c.hacerElClon(this);    		// copia estado común (privados incluidos)
	    c.despuesDeLaCopia(this);       // hook para que subclases copien lo suyo
	    return c;
	}
	
	
	
}
