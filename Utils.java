public class Utils {
   public static final TransformFunction projXY = (u, f) -> new V3(
      u.coord[0], u.coord[1], u.coord[2] * f
   );
   public static final TransformFunction projXZ = (u, f) -> new V3(
      u.coord[0], u.coord[1] * f, u.coord[2]
   );
   public static final TransformFunction projYZ = (u, f) -> new V3(
      u.coord[0] * f, u.coord[1], u.coord[2]
   );
   public static final TransformFunction refX = (u, f) -> new V3(
      u.coord[0] * f, u.coord[1], u.coord[2]
   );
   public static final TransformFunction refY = (u, f) -> new V3(
      u.coord[0], u.coord[1] * f, u.coord[2]
   );
   public static final TransformFunction refZ = (u, f) -> new V3(
      u.coord[0], u.coord[1], u.coord[2] * f
   );
   public static final TransformFunction rotX = (u, a) -> new V3(
      u.coord[0],
      u.coord[1] * Math.cos(a) + u.coord[2] * Math.sin(a),
      u.coord[2] * Math.cos(a) - u.coord[1] * Math.sin(a)
   );
   public static final TransformFunction rotY = (u, a) -> new V3(
      u.coord[0] * Math.cos(a) - u.coord[2] * Math.sin(a),
      u.coord[1],
      u.coord[2] * Math.cos(a) + u.coord[0] * Math.sin(a)
   );
   public static final TransformFunction rotZ = (u, a) -> new V3(
      u.coord[0] * Math.cos(a) + u.coord[1] * Math.sin(a),
      u.coord[1] * Math.cos(a) - u.coord[0] * Math.sin(a),
      u.coord[2]
   );
   public static final TransformFunction scale = (u, f) -> new V3(
      u.coord[0] * f,
      u.coord[1] * f,
      u.coord[2] * f
   );
   public static final TranslateFunction translate = (u, p) -> new V3(
      u.coord[0] + p.coord[0], 
      u.coord[1] + p.coord[1], 
      u.coord[2] + p.coord[2]
   );
   public static final ShearFunction shearOnX = (u, s, t) -> new V3(
      u.coord[0], u.coord[1] + u.coord[0] * s, u.coord[2] + u.coord[0] * t
   );
   public static final ShearFunction shearOnY = (u, s, t) -> new V3(
      u.coord[0] + u.coord[1] * s, u.coord[1], u.coord[2] + u.coord[1] * t
   );
   public static final ShearFunction shearOnZ = (u, s, t) -> new V3(
      u.coord[0] + u.coord[2] * s, u.coord[1] + u.coord[2] * t, u.coord[2]
   );
}
interface TransformFunction {
   V3 apply(V3 u, double f);
}
interface TranslateFunction {
   V3 apply(V3 u, V3 p);
}
interface ShearFunction {
   V3 apply(V3 u, double t, double s);
}
class V3 {
   double[] coord = new double[3];

   V3(double x, double y, double z) {
      coord[0] = x;
      coord[1] = y;
      coord[2] = z;
   }

   @Override
   public String toString() {
      return String.format("%.2f",coord[0]) + " " + String.format("%.2f",coord[1]) + " " + String.format("%.2f",coord[2]);
   }
}