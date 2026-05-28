package uva.poo.practica2;

/**
 * Plaza de aparcamiento equipada con un {@link Cargador}.
 * <p>
 * Esta implementación extiende a {@link Plaza} y compone un cargador eléctrico
 * que permanece inmutable durante la vida de la plaza {@code final}.
 * </p>
 *s
 * @author alvgome
 * @author quegome
 */
public class PlazaConCargador extends Plaza {
	
	/**Cargador asociado a la plaza*/
	private final Cargador cargador;
	
	/**
	 * Crea una plaza con cargador
	 * 
	 * @param id identificador único de la plaza (no negativo, validación heredada en {@link Plaza#Plaza(int)})
     * @param cargador cargador eléctrico que equipa la plaza 
     * @throws NullPointerException si el cargador es null
	 */
	public PlazaConCargador(int id, Cargador cargador) {
		super(id);
		if (cargador == null)throw new NullPointerException("El cargador no puede ser Null");
		this.cargador=cargador;
	}
	
	/**
     * Indica si la plaza dispone de cargador.
     *
     * @return siempre {@code true}
     */
	@Override
	public boolean tieneCargador() {
		return true;
	}
	/**
     * Devuelve el cargador asociado a esta plaza.
     *
     * @return el {@link Cargador} de la plaza 
     */
	@Override
	public Cargador getCargador() {
		return cargador;
	}
	
	 /**
     * Comprueba si el vehículo indicado puede ocupar esta plaza.
     * <p>
     * La comprobación se delega en el propio vehículo (doble despacho):
     * {@code v.plazaCompatible(this)}. De este modo, cada tipo de
     * {@link Vehiculo} decide su compatibilidad con plazas con cargador.
     * </p>
     *
     * @param v vehículo a comprobar 
     * @return {@code true} si el vehículo es compatible con esta plaza; {@code false} en caso contrario
     * @throws NullPointerException si el vehiculo es null
     * @see Vehiculo#plazaCompatible(Plaza)
     */
	@Override
	public boolean admite(Vehiculo v) {
		if (v == null)
	        throw new NullPointerException("vehiculo null");
	    
	    return v.plazaCompatible(this);
	}
	@Override
	protected Plaza clonDelObjeto() {
	    return new PlazaConCargador(this.getID(), this.cargador);
	}
	


}
