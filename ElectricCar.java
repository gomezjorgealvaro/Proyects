package uva.poo.practica2;

/**
 * Implementación concreta de un coche eléctrico del sistema EcoRiderCity.
 * <p>
 * Extiende a {@link VehiculoElectrico} con funcionalidades:
 * </p>
 * <ul>
 *   <li>Compatibilidad con plazas y cargadores (tipo de corriente y rango de potencias admitidas).</li>
 *   <li>Cálculo de autonomía a partir de capacidad de batería y consumo medio.</li>
 *   <li>Cálculo de fianza y coste de recarga despues del alquiler.</li>
 *   <li>Rango de ocupantes permitido para un coche eléctrico.</li>
 * </ul>
 *s
 * @author alvgome
 * @author quegome
 * @see Vehiculo
 * @see VehiculoElectrico
 * @see Cargador
 * @see TipoCorriente
 */
public class ElectricCar extends VehiculoElectrico{

	private double capacidadBateriaCoche;
	private double consumoMedio;
	private int numOcupantes;
	
	
	/**
     * Crea un coche eléctrico con marca, modelo, matrícula, consumo y capacidad de batería.
     *
     * @param marca    marca del vehículo (no vacía)
     * @param modelo   modelo del vehículo (no vacío)
     * @param matricula matrícula 
     * @param consumoMedio consumo medio en kWh/100 km (estrictamente positivo)
     * @param capacidadBateriaCoche capacidad útil de batería en kWh capacidad mayor que 30 y menor que 65
     * @throws IllegalArgumentException si alguna validación heredada de {@link Vehiculo} falla,
     *                                  si consumoMedio es negativo o si la capacidad está fuera del rango permitido
     */
	public ElectricCar(String marca, String modelo, String matricula,
			double consumoMedio, double capacidadBateriaCoche) {
		super(marca, modelo, matricula);
		
		setCapacidadBateria(capacidadBateriaCoche);
		setConsumoMedio(consumoMedio);
	}
	/**
     * Indica si una {@link Plaza} es compatible con este vehículo eléctrico.
     * <p>
     * Un coche eléctrico necesita una plaza con cargador; adicionalmente, el
     * cargador debe ser compatible tanto en tipo de corriente como en potencia.
     * </p>
     *
     * @param plaza plaza donde se va a aparcar
     * @return {@code true} si la plaza tiene cargador y este es compatible; {@code false} si no hay cargador
     */
	@Override
	public boolean plazaCompatible(Plaza plaza) {
		if(!plaza.tieneCargador())
			throw new IllegalStateException("La plaza no tiene cargador");
		return ( admiteCorriente(plaza.getCargador().getCorriente()) && admitePotenciaDeRecarga(plaza.getCargador()));
	}
	
	/**
     * Calcula la autonomía estimada en kilómetros.

     *
     * @return autonomía estimada en km
     * @throws IllegalStateException si los parámetros internos no permiten el cálculo, por ejemplo consumo menor o igual 0
     */
	@Override
	public double kmAutonomia() {
		return (capacidadBateriaCoche/consumoMedio)*100;
	}
	
	/**
     * Devuelve la fianza a pagar para este vehículo.
     
     *
     * @param fianza fianza base fijada por la ciudad (estrictamente positiva)
     * @return fianza total = fianza + (0.05 * fianza * numOcupantes)}
     * @throws IllegalArgumentException si fianza es negativa
     */
	@Override
	public double getFianzaAPagar(double fianza) {
		
		if(fianza <= 0)
			throw new IllegalArgumentException("La fianza tiene que ser mayor que 0");
		
		double porcentaje = (5*fianza)/100;
		return fianza + (porcentaje*numOcupantes);
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
		double porcentaje = (100*gasto)/capacidadBateriaCoche;
		
		return tarifa*capacidadBateriaCoche*(100-porcentaje)/100;
		
	}
	/**
     * Establece la capacidad útil de batería del coche eléctrico.
     *
     * @param capacidadBateria capacidad en kWh capacidad mayor que 30 y menor que 65
     * @throws IllegalArgumentException si la capacidad está fuera del rango permitido
     */
	@Override
	public void setCapacidadBateria(double capacidadBateria) {
		if(capacidadBateria < 30 || capacidadBateria > 65)
			throw new IllegalArgumentException("La capacidad de bateria de los coches electricos tiene que estar entre 30 y 65 k W");
		this.capacidadBateriaCoche = capacidadBateria;	
	}
	 /**
     * Indica si este coche admite la potencia del {@link Cargador} indicado.
     *
     * @param cargador cargador a evaluar 
     * @return {@code true} si la potencia del cargador está dentro de los límites definidos; {@code false} en caso contrario
     */
	@Override
	public boolean admitePotenciaDeRecarga(Cargador cargador) {
	    if(cargador == null) {
	        throw new NullPointerException("La potencia de recarga no puede ser null");
	    }
	    
	    double potencia = cargador.getPotenciaRecarga();
	    
	    if(TipoCorriente.CONTINUA.equals(cargador.getCorriente())) {
	        return potencia >= 22 && potencia <= 100;
	    } else {
	        return potencia <= 9 && potencia >=3.7;
	    }
	}
	/**
     * Indica si el coche admite el tipo de corriente CC o CA.
     *
     * @param corriente tipo de corriente (no nulo)
     * @return {@code true} para {@link TipoCorriente#ALTERNA} y {@link TipoCorriente#CONTINUA}
     * @throws NullPointerException si la corriente es null
     */
	@Override
	public boolean admiteCorriente(TipoCorriente corriente) {
		 return corriente == TipoCorriente.ALTERNA || corriente == TipoCorriente.CONTINUA;
	}
	/**
     * Establece el consumo medio del coche eléctrico.
     *
     * @param consumoMedio consumo en kWh/100 km 
     * @throws IllegalArgumentException si consumoMedio es cero o negativo
     */
	@Override
	public void setConsumoMedio(double consumoMedio) {
		if(consumoMedio <= 0)
			throw new IllegalArgumentException("EL consumo medio no puede ser cero o negativo");
		
		this.consumoMedio = consumoMedio;
	}
	/**
     * Actualiza los contadores de kilómetros asegurando que la distancia no supera la autonomía disponible.
     *
     * @param distancia distancia recorrida en km (no negativa y menor o igual autonomía actual)
     * @throws IllegalArgumentException si distancia es negativa o mayor que {@link #kmAutonomia()}
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
     * @param numOcupantes número de ocupantes ocupantes mayor que 1 y menor que 5
     * @throws IllegalArgumentException si el número de ocupantes está fuera del rango permitido
     */
	@Override
	public void setNumOcupantes(int numOcupantes) {
		if(numOcupantes < 1 || numOcupantes > 5) 
			throw new IllegalArgumentException("El numero de ocupantes del coche tiene que estar entre 1 y 5");
		
		this.numOcupantes = numOcupantes;
	}
	
	/**
	 * Devuelve el consumoMedio de un coche electrico en sus respectivas unidades
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
		return capacidadBateriaCoche;
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
	    return "ElectricCar{" +
	           "marca='" + getMarca() + '\'' +
	           ", modelo='" + getModelo() + '\'' +
	           ", matricula='" + getMatricula() + '\'' +
	           ", capacidadBateria=" + capacidadBateriaCoche + "kWh" +
	           ", consumoMedio=" + consumoMedio + "kWh/100km" +
	           ", ocupantes=" + numOcupantes +
	           '}';
	}
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false; 
	    ElectricCar that = (ElectricCar) o;
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
	    ElectricCar c = new ElectricCar(getMarca(), getModelo(), getMatricula(),
	                                    this.consumoMedio, this.capacidadBateriaCoche);
	    c.setNumOcupantes(this.numOcupantes);
	    return c;
	}

	@Override
	protected void despuesDeLaCopia(Vehiculo src) {
	    super.despuesDeLaCopia(src);               
	    ElectricCar s = (ElectricCar) src;
	    this.numOcupantes = s.numOcupantes;
	}

	

}
