package uva.poo.practica2;

/**
 * Clase que representa el Cargador que se usa en las PlazasConCargador.
 * <p>
 * Cada cargador tiene un identificador unico.<br>
 * Una potencia de corriente que puede ser diferente entre cada cargador y cumple con los requisitos del tipo de corriente.<br>
 * Una tarifa en euros que varía y se usa para calcular el coste del uso de un coche.<br>
 * Un tipo de corritente que puede ser {@link TipoCorriente#ALTERNA} o {@link TipoCorriente#CONTINUA}.<br>
 * <p>
 * @author quegome
 * @author alvgome
 *s
 */
public class Cargador {
	
	
	private int id;
	private double potenciaRecarga;
	private double tarifaEnEuros;
	private TipoCorriente corriente;
	
	/**
	 * Contructor que inicializa un cargador.
	 * @param id identificador unico para cada cargador.
	 * @param potenciaRecarga que puede ser diferente para cada cargador cumpliendo requisitos.
	 * @param tarifaEnEuros que puede ser diferente para cada cargador.
	 * @param corriente que puede ser {@link TipoCorriente#ALTERNA} o {@link TipoCorriente#CONTINUA}
	 * @throws IllegalArgumentException Si el identificador es negativo.
	 * @throws IllegalArgumentException si para corriente {@link TipoCorriente#ALTERNA} la potencia no se encuentra entre 3.7 y 22.
	 * @throws IllegalArgumentException si para corriente {@link TipoCorriente#CONTINUA} la potencia es menor a 22.
     * @throws NullPointerException si la corriente es null.
     * @throws IllegalArgumentException si el tipo de corriente es diferente a {@link TipoCorriente#ALTERNA} o {@link TipoCorriente#CONTINUA}.
     * @throws IllegalArgumentException si la tarifa es menor que 0.
	*/
	public Cargador(int id, double potenciaRecarga, double tarifaEnEuros, TipoCorriente corriente) {
		
		setId(id);
		setTarifaEnEuros(tarifaEnEuros);
		setCorriente(corriente);
		setPotencia(potenciaRecarga);
	}
	/**
	 * Se estabece un id para cada cargador.
	 * @param id identificador unico para cada plaza
	 * @throws IllegalArgumentException Si el identificador es negativo.
	 */
	public void setId(int id) {
		if(id < 0)
			throw new IllegalArgumentException("El identificador no puede ser negativo");
		
		this.id = id;
	}
	/**
	 * Se establece una potencia de recarga cumpliendo unos requisitos.
	 * @param potenciaRecarga segun tipo de corriente
	 * @throws IllegalArgumentException si para corriente {@link TipoCorriente#ALTERNA} la potencia no se encuentra entre 3.7 y 22.
	 * @throws IllegalArgumentException si para corriente {@link TipoCorriente#CONTINUA} la potencia es menor a 22.
	 */
    public void setPotencia(double potenciaRecarga) {
    	
        if (this.getCorriente().equals(TipoCorriente.ALTERNA) && (potenciaRecarga < 3.7 || potenciaRecarga > 22))
            throw new IllegalArgumentException("La potencia de una corriente alterna no puede ser menor que 3.7 y mayor que 22");
        
        if(this.getCorriente().equals(TipoCorriente.CONTINUA) && (potenciaRecarga < 22))
        	throw new IllegalArgumentException("La potencia de una corriente continua no puede ser menor que 22");
        
        this.potenciaRecarga = potenciaRecarga;
    }
    /**
     * Establece la tarifa en euros.
     * @param tarifaEnEuros tarifa que se aplica 
     * @throws IllegalArgumentException si la tarifa es menor que 0.
     */
    public void setTarifaEnEuros(double tarifaEnEuros) {
        if (tarifaEnEuros < 0)
            throw new IllegalArgumentException("La tarifa no puede ser negativa");
        this.tarifaEnEuros = tarifaEnEuros;
    }
    /**
     * Establece el tipo de corriente con la que va a trabajar un cargador
     * @param corriente puede ser {@link TipoCorriente#ALTERNA} o {@link TipoCorriente#CONTINUA}.
     * @throws NullPointerException si la corriente es null.
     */
    public void setCorriente(TipoCorriente corriente) {
    	if (corriente == null)
    	    throw new NullPointerException("Corriente no puede ser null");
    	
    	this.corriente = corriente;

    }
    /**
     * Devuelve el id de un cargador.
     * @return int id.
     */
    public int getId() {
    	return id;
    }
    /**
     * Devuelve la potencia que tiene un cargador.
     * @return double potenciaRecarga.
     */
    public double getPotenciaRecarga() {
    	return potenciaRecarga;
    }
    /**
     * Devuelve la tarifa con la que trabaja un Cargador.
     * @return double tarifaEnEuros
     */
    public double getTarifaEnEuros() {
    	return tarifaEnEuros;
    }
    /**
     * Devuelve el tipo de corriente con el que trabaja un cargador.
     * @return {@link TipoCorriente#ALTERNA} o {@link TipoCorriente#CONTINUA}
     */
    public TipoCorriente getCorriente() {
    	return corriente;
    }
    
    @Override
    public String toString() {
        return "Cargador{" +
               "id=" + id +
               ", corriente=" + corriente +
               ", potenciaRecarga=" + potenciaRecarga +
               ", tarifaEnEuros=" + tarifaEnEuros +
               '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cargador other = (Cargador) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    protected Cargador clonDelObjeto() {
        return new Cargador(this.id, this.potenciaRecarga, this.tarifaEnEuros, this.corriente);
    }

    protected void despuesDeLaCopia(Cargador src) {
    }

    protected final void hacerElClon(Cargador src) {
        this.id = src.id;
        this.potenciaRecarga = src.potenciaRecarga;
        this.tarifaEnEuros = src.tarifaEnEuros;
        this.corriente = src.corriente;
    }

    @Override
    public final Cargador clone() {
        Cargador c = clonDelObjeto();
        c.hacerElClon(this);
        c.despuesDeLaCopia(this);
        return c;
    }

	

}
