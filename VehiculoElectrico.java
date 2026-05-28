package uva.poo.practica2;

/**
 * Clase abstracta que representa a los vehículos eléctricos del sistema EcoRiderCity.
 * <p>
 * Hereda el comportamiento común de {@link Vehiculo} (identidad, matrícula, contadores
 * de kilómetros, estado en marcha y gestión de plaza) y añade la noción de estado de
 * carga y requisitos de recarga propios de la movilidad eléctrica.
 * </p>
 * @author alvgome
 * @author quegome
 */
public abstract class VehiculoElectrico extends Vehiculo{
	/**
     * Porcentaje de carga actual del vehículo en el rango [0, 100].
     * <p>
     * Por conveniencia de uso, el constructor parte de un 100% de carga inicial.
     * </p>
     */
	private double porcentajeDeCarga;
	
	/**
     * Crea un nuevo vehículo eléctrico con marca, modelo y matrícula.
     * <p>
     * Inicializa el estado de carga al 100%. 
     * Las validaciones de marca, modelo y matrícula se delegan en {@link Vehiculo#Vehiculo(String, String, String)}.
     * </p>
     *
     * @param marca, representa la marca que es el vehiculo
	 * @param modelo, representa el modelo de el vehiculo
	 * @param matricula, representa el numero de matricula del vehiculo, su longitud no puede ser distinta de 7
     * @throws IllegalArgumentException si alguna validación heredada de {@link Vehiculo}
     *                                  no se cumple (p. ej., matrícula inválida)
     */
	protected VehiculoElectrico(String marca, String modelo, String matricula) {
		super(marca, modelo, matricula);
		this.porcentajeDeCarga = 100;
		
	}
	/**
     * Indica si el vehículo admite la potencia de recarga de un cargador concreto.
     *
     * @param corriente cargador a evaluar (no nulo)
     * @return {@code true} si la potencia (y características) del cargador
     *         son compatibles con el vehículo; {@code false} en caso contrario
     */
	public abstract boolean admiteCorriente(TipoCorriente corriente);
	
	/**
     * Establece la capacidad de batería útil del vehículo (por ejemplo, en kWh).
     *
     * @param capacidadBateria capacidad de batería 
     * @throws IllegalArgumentException si capacidadBateria no es válida
     */
	public abstract void setCapacidadBateria(double capacidadBateria);
	
	/**
     * Indica si el vehículo admite la potencia de recarga de un cargador concreto.
     *
     * @param cargador cargador a evaluar (no nulo)
     * @return {@code true} si la potencia (y características) del cargador
     *         son compatibles con el vehículo; {@code false} en caso contrario
     * @throws NullPointerException si el cargador no existe
     */
	public abstract boolean admitePotenciaDeRecarga(Cargador cargador);
	
	/**
     * Devuelve el porcentaje de carga actual del vehículo.
     *
     * @return porcentaje de carga en el rango [0, 100]
     */
	public double getPorcentajeDeCarga() {
		return porcentajeDeCarga;
	}
	@Override
	protected void despuesDeLaCopia(Vehiculo src) {
	    super.despuesDeLaCopia(src);
	    VehiculoElectrico s = (VehiculoElectrico) src; 
	    this.porcentajeDeCarga = s.porcentajeDeCarga;
	}
	
}
