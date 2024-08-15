public class Utils {
   public static final TransformFunction projXY = (u, f) -> new V3(
         u.x, u.y, u.z * f[0]);
   public static final TransformFunction projXZ = (u, f) -> new V3(
         u.x, u.y * f[0], u.z);
   public static final TransformFunction projYZ = (u, f) -> new V3(
         u.x * f[0], u.y, u.z);
   public static final TransformFunction refX = (u, f) -> new V3(
         u.x * f[0], u.y, u.z);
   public static final TransformFunction refY = (u, f) -> new V3(
         u.x, u.y * f[0], u.z);
   public static final TransformFunction refZ = (u, f) -> new V3(
         u.x, u.y, u.z * f[0]);
   public static final TransformFunction rotX = (u, a) -> new V3(
         u.x,
         u.y * Math.cos(a[0]) + u.z * Math.sin(a[0]),
         u.z * Math.cos(a[0]) - u.y * Math.sin(a[0]));
   public static final TransformFunction rotY = (u, a) -> new V3(
         u.x * Math.cos(a[0]) - u.z * Math.sin(a[0]),
         u.y,
         u.z * Math.cos(a[0]) + u.x * Math.sin(a[0]));
   public static final TransformFunction rotZ = (u, a) -> new V3(
         u.x * Math.cos(a[0]) + u.y * Math.sin(a[0]),
         u.y * Math.cos(a[0]) - u.x * Math.sin(a[0]),
         u.z);
   public static final TransformFunction rotZX = (u, a) -> new V3( // combined rotations to avoid object recreation
         u.x * Math.cos(a[0]) + u.y * Math.sin(a[0]),
         (u.y * Math.cos(a[0]) - u.x * Math.sin(a[0])) * Math.cos(a[1]) + u.z * Math.sin(a[1]),
         u.z * Math.cos(a[1]) - (u.y * Math.cos(a[0]) - u.x * Math.sin(a[0])) * Math.sin(a[1]));
   public static final TransformFunction scale = (u, f) -> new V3(
         u.x * f[0],
         u.y * f[0],
         u.z * f[0]);
   public static final TransformFunction translate = (u, d) -> new V3(
         u.x + d[0],
         u.y + d[1],
         u.z + d[2]);
   public static final TransformFunction shearOnX = (u, s) -> new V3(
         u.x, u.y + u.x * s[0], u.z + u.x * s[1]);
   public static final TransformFunction shearOnY = (u, s) -> new V3(
         u.x + u.y * s[0], u.y, u.z + u.y * s[1]);
   public static final TransformFunction shearOnZ = (u, s) -> new V3(
         u.x + u.z * s[0], u.y + u.z * s[1], u.z);
}

interface TransformFunction {
   V3 apply(V3 u, double... vals);
}

class V3 {
   double x, y, z;

   V3(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   @Override
   public String toString() {
      return String.format("%.2f", x) + "/" + String.format("%.2f", y) + "/" + String.format("%.2f", z);
   }
}
