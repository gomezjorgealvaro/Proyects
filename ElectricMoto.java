package uva.poo.practica2;
/**
 * Implementación concreta de una moto eléctrica del sistema EcoRiderCity.
 * <p>
 * Extiende a {@link VehiculoElectrico} con funcionalidades:
 * </p>
 * <ul>
 *   <li>Compatibilidad con plazas y cargadores (tipo de corriente y rango de potencias admitidas).</li>
 *   <li>Cálculo de autonomía a partir de capacidad de batería y consumo medio.</li>
 *   <li>Cálculo de fianza y coste de recarga despues del alquiler.</li>
 *   <li>Rango de ocupantes permitido para una moto eléctrica.</li>
 * </ul>
 *s
 * @author alvgome
 * @author quegome
 * @see Vehiculo
 * @see VehiculoElectrico
 * @see Cargador
 * @see TipoCorriente
 */
public class ElectricMoto extends VehiculoElectrico {
	
	private double capacidadBateriaMoto;
	private double consumoMedio;
	private int numOcupantes;
	/**
     * Crea una moto eléctrica con marca, modelo, matrícula, consumo y capacidad de batería.
     *
     * @param marca    marca del vehículo (no vacía)
     * @param modelo   modelo del vehículo (no vacío)
     * @param matricula matrícula 
     * @param consumoMedio consumo medio en kWh/100 km (estrictamente positivo)
     * @param capacidadBateriaMoto capacidad útil de batería en kWh capacidad mayor que 4 y menor que 11
     * @throws IllegalArgumentException si alguna validación heredada de {@link Vehiculo} falla,
     *                                  si consumoMedio es negativo o si la capacidad está fuera del rango permitido
     */
	public ElectricMoto(String marca, String modelo, String matricula,
			double consumoMedio, double capacidadBateriaMoto) {
		super(marca, modelo, matricula);
		
		setCapacidadBateria(capacidadBateriaMoto);
		setConsumoMedio(consumoMedio);

	}
	/**
     * Indica si una {@link Plaza} es compatible con este vehículo eléctrico.
     * <p>
     * Un turismo eléctrico necesita una plaza con cargador; adicionalmente, el
     * cargador debe ser compatible tanto en tipo de corriente como en potencia.
     * </p>
     *
     * @param plaza plaza candidata donde aparcar (no nula)
     * @return {@code true} si la plaza tiene cargador y este es compatible; 
     * {@code false} en caso contrario si la plaza no tiene cargador
     */
	@Override
	public boolean plazaCompatible(Plaza plaza) {
		if(!plaza.tieneCargador())
			throw new IllegalStateException("La plaza no tiene cargador");
		return (admiteCorriente(plaza.getCargador().getCorriente()) && (admitePotenciaDeRecarga(plaza.getCargador())));
	
	}
	/**
     * Calcula la autonomía estimada en kilómetros.
     * <p>
     * Fórmula: autonomia_km = (capacidad_kWh / consumo_kWh_100km) * 100.
     * </p>
     *
     * @return autonomía estimada en km
     * @throws IllegalStateException si los parámetros internos no permiten el cálculo (p. ej., consumo menor o igual 0)
     */
	@Override
	public double kmAutonomia() {
		return (capacidadBateriaMoto/consumoMedio)*100;
	}
	/**
     * Devuelve la fianza a pagar para este vehículo.
     
     *
     * @param fianza fianza base fijada por la ciudad (estrictamente positiva)
     * @return fianza total =  fianza + (0.1 * fianza * numOcupantes)
     * @throws IllegalArgumentException si la fianza es negativa
     */
	@Override
	public double getFianzaAPagar(double fianza) {
		if(fianza <= 0)
			throw new IllegalArgumentException("La fianza tiene que ser mayor que 0");
		
		double porcentaje = (10*fianza)/100;
		return fianza - porcentaje;
	}
	/**
     * Calcula el coste de la recarga asociada al alquiler en función de los km recorridos.
     * Supone que el coste se calcula con la tarifa del cargador de la plaza asignada y la
     * energía necesaria para devolver la carga a “lleno”.
     *
     *
     * @param kmRecorridos kilómetros recorridos durante el alquiler (no negativos)
     * @return coste de recarga en euros
     * @throws IllegalArgumentException si kmRecorridos es negativo
     * @throws IllegalStateException si no hay plaza asignada o no hay cargador en la plaza
     */
	@Override
	public double obtenerCosteLlenado(double kmRecorridos) {
		
		if(kmRecorridos < 0)
			throw new IllegalArgumentException("Los kilometos recorridos no pueden ser negativos");
		
		double gasto = kmRecorridos*consumoMedio;
		Plaza pl = getPlazaAsignada();
		double tarifa = pl.getCargador().getTarifaEnEuros();
		double porcentaje = (100*gasto)/capacidadBateriaMoto;
		
		return tarifa*capacidadBateriaMoto*(100-porcentaje)/100;
		
	}
	/**
     * Establece la capacidad útil de batería de la moto eléctrica.
     *
     * @param capacidadBateria capacidad en kWh capacidad entre 4 y 11
     * @throws IllegalArgumentException si la capacidad está fuera del rango permitido
     */
	@Override
	public void setCapacidadBateria(double capacidadBateria) {
		if(capacidadBateria < 4 || capacidadBateria > 11 )
			throw new IllegalArgumentException("La bateria tiene que estar entre 4 y 11 kW para las motos");
		
		this.capacidadBateriaMoto = capacidadBateria;
	}
	/**
     * Indica si esta moto admite la potencia del {@link Cargador} indicado.
     *
     * @param cargador cargador a evaluar 
     * @return {@code true} si la corriente del cargador es Alterna.
     */
	@Override
	public boolean admitePotenciaDeRecarga(Cargador cargador) {
		
		if(admiteCorriente(cargador.getCorriente())){	
			return cargador.getPotenciaRecarga() == 6.9;
		}
		return false;
		
		
	}
	/**
     * Establece el consumo medio de la moto eléctrica.
     *
     * @param consumoMedio consumo en kWh/100 km 
     * @throws IllegalArgumentException si consumoMedio es negativo
     */
	@Override
	public void setConsumoMedio(double consumoMedio) {
		if(consumoMedio <= 0)
			throw new IllegalArgumentException("EL consumo medio no puede ser cero o negativo");
		
		this.consumoMedio = consumoMedio;
	}
	/**
     * Indica si la corriente es CC O CA.
     *
     * @param corriente tipo de corriente 
     * @return {@code true} si y solo si {@link TipoCorriente#ALTERNA}
     */
	@Override
	public boolean admiteCorriente(TipoCorriente corriente) {
		return corriente.equals(TipoCorriente.ALTERNA);
	}
	/**
     * Actualiza los contadores de kilómetros asegurando que la distancia no supera la autonomía disponible.
     *
     * @param distancia distancia recorrida en km (no negativa y menor o igual que autonomía actual)
     * @throws IllegalArgumentException si la distancia es negativa o mayor que {@link #kmAutonomia()}
     */
	@Override
	public void actualizarContadores(double distancia) {
		if(distancia > kmAutonomia())
			throw new IllegalArgumentException("El vehiculo no puede recorrer esa distancia");
		
		super.actualizarContadores(distancia);
	}
	 /**
     * Establece el número de ocupantes del coche.
     *
     * @param numOcupantes número de ocupantes entre 1 y 2
     * @throws IllegalArgumentException si el número de ocupantes está fuera del rango permitido
     */
	@Override
	public void setNumOcupantes(int numOcupantes) {
		if(numOcupantes < 1 || numOcupantes >2)
			throw new IllegalArgumentException("El numero de ocupantes de la moto tiene que ser dos");
		this.numOcupantes = numOcupantes;
	}
	
	/**
	 * Devuelve el consumo Medio de una moto electrica en sus respectivas unidades
	 */
	@Override
	public double getConsumoMedio() {
		return consumoMedio;
	}
	
	/**
	 * Devuelve la capacidad de bateria de la moto electrica
	 */
	@Override
	public double getCapacidadBateria() {
		return capacidadBateriaMoto;
	}
	/**
     * Devuelve el número de ocupantes configurado.
     *
     * @return número de ocupantes
     */
	@Override
	public int getNumOcupantes() {
		return numOcupantes;
	}
	@Override
	public String toString() {
	    return "ElectricMoto{" +
	           "marca='" + getMarca() + '\'' +
	           ", modelo='" + getModelo() + '\'' +
	           ", matricula='" + getMatricula() + '\'' +
	           ", capacidadBateria=" + capacidadBateriaMoto + "kWh" +
	           ", consumoMedio=" + consumoMedio + "kWh/100km" +
	           ", ocupantes=" + numOcupantes +
	           '}';
	}
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    ElectricMoto that = (ElectricMoto) o;
	    String m1 = this.getMatricula();
	    String m2 = that.getMatricula();
	    return m1 != null ? m1.equals(m2) : m2 == null;
	}
	@Override
    public int hashCode() {
        return getMatricula().hashCode();
    }
	@Override
	protected Vehiculo clonDelObjeto() {
	    ElectricMoto c = new ElectricMoto(getMarca(), getModelo(), getMatricula(),
	                                      this.consumoMedio, this.capacidadBateriaMoto);
	    c.setNumOcupantes(this.numOcupantes);
	    return c;
	}
	@Override
	protected void despuesDeLaCopia(Vehiculo src) {
	    super.despuesDeLaCopia(src);
	    ElectricMoto s = (ElectricMoto) src;
	    this.numOcupantes = s.numOcupantes;
	}


	
}
