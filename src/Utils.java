/**
 * This class provides utility functions for 3D transformations.
 * It defines several transformation functions as lambda expressions,
 * each implementing the TransformFunction interface.
 */
public class Utils {
    // Projection functions
    public static final TransformFunction projXY = (u, f) -> new V3(u.x, u.y, u.z * f[0]);
    public static final TransformFunction projXZ = (u, f) -> new V3(u.x, u.y * f[0], u.z);
    public static final TransformFunction projYZ = (u, f) -> new V3(u.x * f[0], u.y, u.z);

    // Reflection functions
    public static final TransformFunction refX = (u, f) -> new V3(u.x * f[0], u.y, u.z);
    public static final TransformFunction refY = (u, f) -> new V3(u.x, u.y * f[0], u.z);
    public static final TransformFunction refZ = (u, f) -> new V3(u.x, u.y, u.z * f[0]);

    // Rotation functions
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

    public static final TransformFunction rotZX = (u, a) -> new V3( 
            u.x * Math.cos(a[0]) + u.y * Math.sin(a[0]),
            (u.y * Math.cos(a[0]) - u.x * Math.sin(a[0])) * Math.cos(a[1]) + u.z * Math.sin(a[1]),
            u.z * Math.cos(a[1]) - (u.y * Math.cos(a[0]) - u.x * Math.sin(a[0])) * Math.sin(a[1]));

    // Scaling function
    public static final TransformFunction scale = (u, f) -> new V3(
            u.x * f[0],
            u.y * f[0],
            u.z * f[0]);

    // Translation function
    public static final TransformFunction translate = (u, d) -> new V3(
            u.x + d[0],
            u.y + d[1],
            u.z + d[2]);

    // Shearing functions
    public static final TransformFunction shearOnX = (u, s) -> new V3(
            u.x, u.y + u.x * s[0], u.z + u.x * s[1]);
    public static final TransformFunction shearOnY = (u, s) -> new V3(
            u.x + u.y * s[0], u.y, u.z + u.y * s[1]);
    public static final TransformFunction shearOnZ = (u, s) -> new V3(
            u.x + u.z * s[0], u.y + u.z * s[1], u.z);
}

/**
 * This interface defines a contract for functions that transform a 3D vector.
 */
interface TransformFunction {
    /**
     * Applies a transformation to a given 3D vector.
     * 
     * @param u The vector to transform.
     * @param args An array of double values representing transformation parameters.
     * @return The transformed vector.
     */
    V3 apply(V3 u, double... args);
}

/**
 * This class represents a 3D vector with x, y, and z components.
 */
class V3 {
    double x, y, z;

    /**
     * Constructs a 3D vector with the given x, y, and z components.
     * 
     * @param x The x-component of the vector.
     * @param y The y-component of the vector.
     * @param z The z-component of the vector.
     */
    V3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns a string representation of the vector.
     * 
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return "(" + String.format("%.2f", x) + ", " + String.format("%.2f", y) + ", " + String.format("%.2f", z) + ")";
    }
}