package game.math;

public class Vec2D {
	
	//TODO: Make Vec2D immutable
	
	private double x, y;
	
	public Vec2D() {
		x=y=0;
	}
	
	public Vec2D(Vec2D start, Vec2D end) {
		this.x=end.x-start.x;
		this.y=end.y-start.y;
	}
	
	public Vec2D(double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	public Vec2D(Vec2D v) {
		x=v.x;
		y=v.y;
	}
	
	public void set(double value) {
		x=y=value;
	}
	
	public void set(double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	public void set(Vec2D v) {
		this.x=v.x;
		this.y=v.y;
	}
	
	public void setX(double x) {
		this.x=x;
	}
	
	public void setY(double y) {
		this.y=y;
	}
	
	public void setLength(double newLength) {
		double t=newLength/length();
		this.x*=t;
		this.y*=t;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public boolean isNullVector() {
		return x==0 && y==0;
	}
	
	public void add(Vec2D v) {
		this.x+=v.x;
		this.y+=v.y;
	}
	
	public void sub(Vec2D v) {
		this.x-=v.x;
		this.y-=v.y;
	}
	
	public void add(double x, double y) {
		this.x+=x;
		this.y+=y;
	}
	
	public void sub(double x, double y) {
		this.x-=x;
		this.y-=y;
	}
	
	public void scale(double s) {
		this.x*=s;
		this.y*=s;
	}
	
	public double length() {
		return Math.sqrt(x*x+y*y);
	}
	
	public double lengthSq() {
		return x*x+y*y;
	}
	
	public double distanceTo(Vec2D v) {
		double a=this.x-v.x;
		double b=this.y-v.y;
		return Math.sqrt(a*a+b*b);
	}
	
	public double distanceToSq(Vec2D v) {
		double a=this.x-v.x;
		double b=this.y-v.y;
		return a*a+b*b;
	}
	
	public void abs() {
		x=Math.abs(x);
		y=Math.abs(y);
	}
	
	public void normalize() {
		double inv=1/length();
		x*=inv;
		y*=inv;
	}
	
	public void validate() {
		if (Double.isInfinite(x) || Double.isNaN(x) || Double.isInfinite(y) || Double.isNaN(y)) {
			throw new RuntimeException("Invalid vector: "+this.toString());
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((Vec2D)obj).x==this.x && ((Vec2D)obj).y==this.y;
	}
	
	@Override
	public int hashCode() {
		long l1 = Double.doubleToRawLongBits(x);
		long l2 = Double.doubleToRawLongBits(y);
		long l3 = l1 ^ l2;
		int i1 = (int) (l3 >>> 32);
		int i2 = (int) (l3 & 0x00000000FFFFFFFF);
		return i1 ^ i2;
	}
	
	@Override
	public String toString() {
		return "["+x+", "+y+"]";
	}
	
	public static Vec2D add(Vec2D a, Vec2D b) {
		return new Vec2D(a.x+b.x, a.y+b.y);
	}
	
	public static Vec2D add(Vec2D a, double x, double y) {
		return new Vec2D(a.x+x, a.y+y);
	}
	
	public static Vec2D normalize(Vec2D v) {
		double inv=1/v.length();
		return new Vec2D(v.x*inv, v.y*inv);
	}
	
	public static Vec2D sub(Vec2D a, Vec2D b) {
		return new Vec2D(a.x-b.x, a.y-b.y);
	}
	
	public static double distanceSquared(Vec2D a, Vec2D b) {
		double v = a.x-b.x;
		double u = a.y-b.y;
		return v*v+u*u;
	}
	
	public static Vec2D distanceVec(Vec2D from, Vec2D to) {
		return new Vec2D(to.x-from.x, to.y-from.y);
	}
	
	public static double dot(Vec2D v1, Vec2D v2) {
		return v1.x*v2.x+v1.y*v2.y;
	}
	
	public static double angleBetween(Vec2D v1, Vec2D v2) {
		return Math.acos(dot(v1, v2)/Math.sqrt(v1.lengthSq()*v2.lengthSq()));
	}
	
	public static double pointLineDistance(Vec2D a, Vec2D b, Vec2D p) {
		return Math.abs((p.x-a.x)*(b.y-a.y)-(p.y-a.y)*(b.x-a.x))/Math.hypot(b.x-a.x, b.y-a.y);
	}
	
	public static double pointLineSegmentDistanceSquared(Vec2D a, Vec2D b, Vec2D p) {
		double l2 = distanceSquared(a, b);
		if (l2 == 0.0) {
			return distanceSquared(p, a);
		}
		double t = ((p.x-a.x)*(b.x-a.x) + (p.y-a.y)*(b.y-a.y)) / l2;
		if (t < 0) {
			return distanceSquared(p, a);
		} else if (t > 0) {
			return distanceSquared(p, b);
		} else {
			Vec2D q = new Vec2D(a.x + t*(b.x-a.x), 
								a.y + t*(b.y-a.y));
			return distanceSquared(p, q);
		}
		
	}
	
	public static double pointLineSegmentDistance(Vec2D a, Vec2D b, Vec2D p) {
		return Math.sqrt(pointLineSegmentDistanceSquared(a, b, p));
	}
}
