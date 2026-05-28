package uva.poo.practica2;


/**
 * Plaza de aparcamiento normal.
 * <p>
 * Esta implementación extiende a {@link Plaza}.
 * </p>
 *s
 * @author alvgome
 * @author quegome
 */
public class PlazaSinCargador extends Plaza {

	/**
	 * Crea una plaza con cargador
	 * 
	 * @param id identificador único de la plaza (no negativo, validación heredada en {@link Plaza#Plaza(int)})
	 */
	public PlazaSinCargador(int id) {
		super(id);
	}
	
	/**
     * Indica si la plaza dispone de cargador.
     *
     * @return siempre {@code false}
     */
	@Override
	public boolean tieneCargador() {
		return false;
	}
	/**
     * Devuelve el cargador asociado a esta plaza.
     *
     * @return (siempre {@code null})
     */
	@Override
	public Cargador getCargador() {
		return null;
	}
	 
    /**
	 * Determina si esta plaza sin cargador admite el vehículo indicado.
	 *
	 * <p>La decisión se delega en el propio vehículo
     *
     * @param v vehículo a comprobar 
     * @return {@code true} si el vehículo es compatible con esta plaza; {@code false} en caso contrario
     * @throws NullPointerException si el vehiculo v es null
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
	    return new PlazaSinCargador(this.getID());
	}


}
