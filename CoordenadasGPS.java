package uva.poo.practica2;
/**
 * Representa una posición geográfica usada por el sistema {@code EcoRiderParking}.
 * <p>
 * La coordenada puede almacenarse en dos formatos:
 * <ul>
 *   <li>Grados decimales (latitud y longitud en {@code double}).</li>
 *   <li>Grados sexagesimales (grados, minutos, segundos y hemisferio).</li>
 * </ul>
 * La clase permite:
 * <ul>
 *   <li>Crear una coordenada a partir de grados decimales.</li>
 *   <li>Crear una coordenada a partir de una cadena en formato GMS (por ejemplo {@code "41º 39' 08'' N"}).</li>
 *   <li>Convertir las componentes GMS a decimal.</li>
 *   <li>Convertir las componentes decimales a GMS.</li>
 *   <li>Calcular la distancia entre dos coordenadas mediante la fórmula de Haversine.</li>
 * </ul>
 *
 * @author alvgome
 * @author quegome
 */
public class CoordenadasGPS {

	private double decimalesLat;
	private double decimalesLon;
	

    /**
     * Construye una coordenada geográfica a partir de su latitud y longitud
     * en grados decimales.
     * <p>
     * Se validan los rangos habituales:
     * <ul>
     *   <li>Latitud: [-90, 90]</li>
     *   <li>Longitud: [-180, 180]</li>
     * </ul>
     *
     * @param latGD latitud en grados decimales
     * @param lonGD longitud en grados decimales
     * @throws IllegalArgumentException si la latitud o la longitud están fuera de rango
     */
	public CoordenadasGPS(double latGD, double lonGD) {
	    validarGD(latGD, lonGD);
	    this.decimalesLat = latGD;
	    this.decimalesLon = lonGD;
	  }
	

	public CoordenadasGPS (String latGms, String lonGms) {
	    double latGD = parseGMS(latGms, true);   // true = latitud
	    double lonGD = parseGMS(lonGms, false);  // false = longitud
	   	this.decimalesLat = latGD;
	   	this.decimalesLon = lonGD;
	}
	  
	private static void validarGD(double lat, double lon) {
		  if (lat < -90 || lat > 90) throw new IllegalArgumentException("Latitud fuera de rango");
		  if (lon < -180 || lon > 180) throw new IllegalArgumentException("Longitud fuera de rango");
	}
	
	
	public static double parseGMS(String gms, boolean esLatitud) {
		if (gms == null) throw new IllegalArgumentException("GMS null");
		String s = gms.trim().replace('°','º').replace(",",".").replace(" ","");
		int iDeg = s.indexOf('º');
		int iMin = s.indexOf('\'', iDeg + 1);
		int iSec = s.indexOf("''", iMin + 1);
		validarFormatoBasico(s, iDeg, iMin, iSec);
		char hemi = Character.toUpperCase(s.charAt(s.length() - 1));
		int grados = Integer.parseInt(s.substring(0, iDeg));
		int minutos = Integer.parseInt(s.substring(iDeg + 1, iMin));
		double segundos = Double.parseDouble(s.substring(iMin + 1, iSec));
		validarRangosGMS(grados, minutos, segundos, hemi, esLatitud);
		double dec = grados + minutos / 60.0 + segundos / 3600.0;
		if ((esLatitud && hemi == 'S') || (!esLatitud && hemi == 'O')) dec = -dec;
		return dec;
	}
	private static void validarFormatoBasico(String s, int iDeg, int iMin, int iSec) {
	    if (iDeg < 0) throw new IllegalArgumentException("Falta º");
	    if (iMin < 0) throw new IllegalArgumentException("Falta '");
	    if (iSec < 0) throw new IllegalArgumentException("Faltan ''");
	    if (iSec + 2 >= s.length()) throw new IllegalArgumentException("Falta hemisferio");
	}
	private static void validarRangosGMS(int g, int m, double s, char h, boolean esLat) {
	    if (m < 0 || m >= 60) throw new IllegalArgumentException("Minutos fuera de rango");
	    if (s < 0 || s >= 60) throw new IllegalArgumentException("Segundos fuera de rango");
	    if (esLat) {
	        if (h != 'N' && h != 'S') throw new IllegalArgumentException("Hemisferio lat N/S");
	        if (g < 0 || g > 90 || (g == 90 && (m > 0 || s > 0))) throw new IllegalArgumentException("Lat fuera de rango");
	    } else {
	        if (h != 'E' && h != 'O') throw new IllegalArgumentException("Hemisferio lon E/O");
	        if (g < 0 || g > 180 || (g == 180 && (m > 0 || s > 0))) throw new IllegalArgumentException("Lon fuera de rango");
	    }
	} 

	public String toLatGMS() { return toGMS(decimalesLat, true); }
	public String toLonGMS() { return toGMS(decimalesLon, false); }

	private static String toGMS(double gd, boolean esLat) {
	  int decimales = 2;
	  double factor = Math.pow(10, decimales); 
	  char hemi = esLat ? (gd >= 0 ? 'N' : 'S') : (gd >= 0 ? 'E' : 'O');
	  double abs = Math.abs(gd);
	  int grados = (int) abs;
	  double minTot = (abs - grados) * 60;
	  int minutos = (int) minTot;
	  double segundos = Math.round(((minTot - minutos) * 60)*factor) / factor;
	  return grados + "º " + minutos + "' " + segundos + "'' " + hemi;
	}
	
	
	public double getLatGD() {
	    return decimalesLat;
	}
	public double getLonGD() {
	    return decimalesLon;
	}

	
	public String getLatGMS() {
	    return toGMS(decimalesLat, true);
	}
	public String getLonGMS() {
	    return toGMS(decimalesLon, false);
	}

	
	private double latEnDecimales() {
	    return decimalesLat;
	}
	private double lonEnDecimales() {
	    return decimalesLon;
	}
	
	
    
    /**
     * Calcula la distancia en metros entre esta coordenada y la coordenada dada
     * como parámetro utilizando la fórmula de Haversine.
     * <p>
     * Si alguna de las dos coordenadas se construyó en formato GMS, se convierte
     * internamente a grados decimales antes de realizar el cálculo.
     *
     * @param otrasCoordenadas coordenada destino, no puede ser {@code null}
     * @return distancia en metros entre las dos coordenadas
     * @throws NullPointerException si {@code otrasCoordenadas} es {@code null}
     */
	public double calcularDistancia(CoordenadasGPS otrasCoordenadas) {
	    double radio = 6371000;
	    if (otrasCoordenadas == null) throw new NullPointerException("Coordenadas Null");
	    
	    double lat1GD = this.latEnDecimales();
	    double lon1GD = this.lonEnDecimales();
	    double lat2GD = otrasCoordenadas.latEnDecimales();
	    double lon2GD = otrasCoordenadas.lonEnDecimales();
	    
	    double lat1 = Math.toRadians(lat1GD);
        double lon1 = Math.toRadians(lon1GD);
        double lat2 = Math.toRadians(lat2GD);
        double lon2 = Math.toRadians(lon2GD);
        
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.asin(Math.min(1.0, Math.sqrt(a)));
        
        return  c * radio;
	}
	
	@Override
	public String toString() {
	    double lat = latEnDecimales();
	    double lon = lonEnDecimales();
	    return ("lat="+lat+",lon="+lon);
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;

	    CoordenadasGPS other = (CoordenadasGPS) obj;

	    final double EPS = 1e-9;
	    double lat1 = this.latEnDecimales();
	    double lon1 = this.lonEnDecimales();
	    double lat2 = other.latEnDecimales();
	    double lon2 = other.lonEnDecimales();

	    return Math.abs(lat1 - lat2) <= EPS && Math.abs(lon1 - lon2) <= EPS;
	}
	@Override
	public int hashCode() {
	    double lat = latEnDecimales();
	    double lon = lonEnDecimales();

	    final double STEP = 1e-9; 
	    long latQ = Math.round(lat / STEP);
	    long lonQ = Math.round(lon / STEP);

	    int result = 17;
	    result = 31 * result + Long.hashCode(latQ);
	    result = 31 * result + Long.hashCode(lonQ);
	    return result;
	}
	protected CoordenadasGPS clonDelObjeto() {
	    return new CoordenadasGPS(this.decimalesLat, this.decimalesLon);
	}

	protected void despuesDeLaCopia(CoordenadasGPS src) {
	}

	protected final void hacerElClon(CoordenadasGPS src) {
	    this.decimalesLat = src.decimalesLat;
	    this.decimalesLon = src.decimalesLon;
	}

	@Override
	public final CoordenadasGPS clone() {
	    CoordenadasGPS c = clonDelObjeto();
	    c.hacerElClon(this);
	    c.despuesDeLaCopia(this);
	    return c;
	}

	

	
	
}
