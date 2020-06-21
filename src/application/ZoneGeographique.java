package application;

public class ZoneGeographique {
	
	private float lat;
	private float lon;
	
	public ZoneGeographique(float lat, float lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	@Override
	public String toString() {
		String res="";
		res = res +this.lat + ";" + this.lon;
		return res;
	}
	
	public float getLat() {
		return this.lat;
	}
	public float getLong() {
		return this.lon;
	}
}
