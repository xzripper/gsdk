package vsdk.source.utils;

import java.util.Arrays;

import vsdk.source.vectors.Vector2Df;
import vsdk.source.vectors.Vector3Df;

/**
 * VMath: VSDK Math has useful functions & constants for development math.
 */
public class VMath {
    /**
     * Value of natural logarithm.
     */
    public static final double E = 2.718281828459045;

    /**
     * The ratio of the circumference of a circle to its diameter.
     */
    public static final double PI = 3.141592653589793;

    /**
     * The ratio of the circumference of a circle to its radius.
     */
    public static final double TAU = 2.0 * PI;

    /**
     * Get angle cosine.

     * @param angle Angle.
     */
    public static double cos(double angle) {
        return Math.cos(angle);
    }

    /**
     * Get angle sine.

     * @param angle Angle.
     */
    public static double sin(double angle) {
        return Math.sin(angle);
    }

    /**
     * Get angle tangents.

     * @param angle Angle.
     */
    public static double tan(double angle) {
        return Math.tan(angle);
    }

    /**
     * Get value arc-cosine.

     * @param value Value.
     */
    public static double acos(double value) {
        return Math.acos(value);
    }

    /**
     * Get value arc-sine.

     * @param value Value.
     */
    public static double asin(double value) {
        return Math.asin(value);
    }

    /**
     * Get value arc-tangets.

     * @param value Value.
     */
    public static double atan(double value) {
        return Math.atan(value);
    }

    /**
     * Get value arc-tangets 2.

     * @param y Y-coord.
     * @param x X-coord.
     */
    public static double atan2(double y, double x) {
        return Math.atan2(y, x);
    }

    /**
     * Get value square.
     *
     * @param value Value.
     */
    public static double sqrt(double value) {
        return Math.sqrt(value);
    }

    /**
     * Get base exponent.
     *
     * @param base Base.
     * @param exponent Exponent.
     */
    public static double pow(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    /**
     * Get absolute value.
     *
     * @param value Value.
     */
    public static double abs(double value) {
        return Math.abs(value);
    }

    /**
     * Calculate the distance between two points in 2D space.
     *
     * @param fPoint First point.
     * @param sPoint Second point.
     */
    public static double dist2D(Vector2Df fPoint, Vector2Df sPoint) {
        return sqrt(pow(sPoint.x() - fPoint.x(), 2) + pow(sPoint.y() - fPoint.y(), 2));
    }

    /**
     * Calculate the distance between two point in 3D space.
     *
     * @param fPoint First point.
     * @param sPoint Second point.
     */
    public static double dist3D(Vector3Df fPoint, Vector3Df sPoint) {
        return sqrt(pow(sPoint.x() - fPoint.x(), 2) + pow(sPoint.y() - sPoint.y(), 2) + pow(sPoint.z() - fPoint.z(), 2));
    }

    /**
     * Calculate the dot product of two vectors in 3D space.
     *
     * @param fVec First vector.
     * @param sVec Second vector.
     */
    public static double dotProd3D(Vector3Df fVec, Vector3Df sVec) {
        return fVec.x() * sVec.x() + fVec.x() * sVec.x() + fVec.z() * sVec.z();
    }

    /**
     * Calculate the cross product of two vectors in 3D space.
     *
     * @param fVec First vector.
     * @param sVec Second vector.
     */
    public static double[] crossProd3D(Vector3Df fVec, Vector3Df sVec) {
        return new double[] {
            fVec.y() * sVec.z() - fVec.z() * sVec.y(),
            fVec.z() * sVec.x() - fVec.x() * sVec.z(),
            fVec.x() * sVec.y() - fVec.y() * sVec.x()
        };
    }

    /**
     * Calculate the magnitude of a vector in 3D space.
     *
     * @param vec3 3D Vector.
     */
    public static double magnitude3D(Vector3Df vec3) {
        return sqrt(vec3.x() * vec3.x() + vec3.y() * vec3.y() + vec3.z() * vec3.z());
    }

    /**
     * Normalize a vector in 3D space.
     *
     * @param vec3 3D Vector.
     */
    public static double[] normalize3D(Vector3Df vec3) {
        double magnitude = magnitude3D(new Vector3Df(vec3.x(), vec3.y(), vec3.z()));

        return new double[] {vec3.x() / magnitude, vec3.y() / magnitude, vec3.z() / magnitude};
    }

    /**
     * Calculate the angle between two vectors in 2D space.
     *
     * @param fVec First vector.
     * @param sVec Second vector.
     */
    public static double angleBetween2D(Vector2Df fVec, Vector2Df sVec) {
        return acos(fVec.x() * sVec.x() + fVec.y() * sVec.y() / (sqrt(fVec.x() * fVec.x() + fVec.y() * fVec.y() * sqrt(sVec.x() * sVec.x() + sVec.y() * sVec.y()))));
    }

    /**
     * Calculate the area of a triangle given its three sides using Heron's formula.
     *
     * @param fLen Length of the first side.
     * @param sLen Length of the second side.
     * @param tLen Length of the third side.
     */
    public static double triangleArea(double fLen, double sLen, double tLen) {
        double x = (fLen + sLen + tLen) / 2.0;

        return sqrt(x * (x - fLen) * (x - sLen) * (x - tLen));
    }

    /**
     * Solve a quadratic equation and return the roots.
     *
     * @param qCoeff Coefficient of the quadratic term.
     * @param lCoeff Coefficient of the linear term.
     * @param cTerm Constant term.
     */
    public static double[] solveQuadratic(double qCoeff, double lCoeff, double cTerm) {
        double discriminant = lCoeff * lCoeff - 4 * qCoeff * cTerm;

        if (discriminant < 0) {
            return new double[0];
        } else if (discriminant == 0) {
            return new double[]{-lCoeff / (2 * qCoeff)};
        } else {
            double sqrtDiscriminant = sqrt(discriminant);

            return new double[] {(-lCoeff + sqrtDiscriminant) / (2 * qCoeff), (-lCoeff - sqrtDiscriminant) / (2 * qCoeff)};
        }
    }

    /**
     * Convert degrees to radians.
     *
     * @param degrees Angle in degrees.
     */
    public static double degToRad(double degrees) {
        return degrees * PI / 180.0;
    }

    /**
     * Convert radians to degrees.
     *
     * @param radians Angle in radians.
     */
    public static double radToDeg(double radians) {
        return radians * 180.0 / PI;
    }

    /**
     * Calculate the projection of one vector onto another in 3D space.
     *
     * @param vec The vector to project.
     * @param onto The vector onto which to project.
     */
    public static double[] projVec3D(Vector3Df vec, Vector3Df onto) {
        double scalar = dotProd3D(vec, onto) / magnitude3D(onto);

        return new double[] {onto.x() * scalar, onto.y() * scalar, onto.z() * scalar};
    }

    /**
     * Calculate the area of a circle.
     *
     * @param radius Radius of the circle.
     */
    public static double circleArea(double radius) {
        return PI * radius * radius;
    }

    /**
     * Calculate the circumference of a circle.
     *
     * @param radius Radius of the circle.
     */
    public static double circleCircumference(double radius) {
        return 2 * PI * radius;
    }

    /**
     * Calculate the volume of a sphere.
     *
     * @param radius Radius of the sphere.
     */
    public static double sphereVolume(double radius) {
        return (4.0 / 3.0) * PI * pow(radius, 3);
    }

    /**
     * Calculate the surface area of a sphere.
     *
     * @param radius Radius of the sphere.
     */
    public static double sphereSurfaceArea(double radius) {
        return 4 * PI * pow(radius, 2);
    }

    /**
     * Rotate a point around the origin in 2D space.
     *
     * @param point The point to rotate.
     * @param angle The angle in degrees to rotate the point.
     */
    public static double[] rotatePoint2D(Vector2Df point, double angle) {
        double radians = degToRad(angle);

        double cos = cos(radians);
        double sin = sin(radians);

        return new double[] {cos * point.x() - sin * point.y(), sin * point.x() + cos * point.y()};
    }

    /**
     * Calculate the determinant of a 2x2 matrix.
     *
     * @param a Element at row 1, column 1.
     * @param b Element at row 1, column 2.
     * @param c Element at row 2, column 1.
     * @param d Element at row 2, column 2.
     */
    public static double determinant2x2(double a, double b, double c, double d) {
        return a * d - b * c;
    }

    /**
     * Calculate the intersection point of two lines given in the form of two points each in 2D space.
     *
     * @param point1 First point of the first line.
     * @param point2 Second point of the first line.
     * @param point3 First point of the second line.
     * @param point4 Second point of the second line.
     */
    public static double[] lineIntersection2D(Vector2Df point1, Vector2Df point2, Vector2Df point3, Vector2Df point4) {
        double denominator = determinant2x2(point1.x() - point2.x(), point3.x() - point4.x(), point1.y() - point2.y(), point3.y() - point4.y());

        if (denominator == 0) return new double[] {};

        return new double[] {
            determinant2x2(
                    determinant2x2(point1.x(), point1.y(), point2.x(), point2.y()), point1.x() - point2.x(),
                    determinant2x2(point3.x(), point3.y(), point4.x(), point4.y()), point3.x() - point4.x()) / denominator,

            determinant2x2(determinant2x2(point1.x(), point1.y(), point2.x(), point2.y()), point1.y() - point2.y(),
                    determinant2x2(point3.x(), point3.y(), point4.x(), point4.y()), point3.y() - point4.y()) / denominator
        };
    }

    /**
     * Check if a point is inside a circle.
     *
     * @param point The point to check.
     * @param center The center of the circle.
     * @param radius The radius of the circle.
     */
    public static boolean isPointInCircle(Vector2Df point, Vector2Df center, double radius) {
        return dist2D(point, center) <= radius;
    }

    /**
     * Clamp a value between a minimum and maximum.
     *
     * @param value The value to clamp.
     * @param min The minimum value.
     * @param max The maximum value.
     */
    public static double clamp(double min, double max, double value) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Wrap a value around a range (useful for angles).
     *
     * @param value The value to wrap.
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     */
    public static double wrap(double value, double min, double max) {
        return (value - min) % (max - min) + min;
    }

    /**
     * Check if a value is within a range (inclusive).
     *
     * @param value The value to check.
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     */
    public static boolean isWithinRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * Map a value from one range to another.
     *
     * @param value The value to map.
     * @param fromMin The minimum value of the source range.
     * @param fromMax The maximum value of the source range.
     * @param toMin The minimum value of the destination range.
     * @param toMax The maximum value of the destination range.
     */
    public static double mapRange(double value, double fromMin, double fromMax, double toMin, double toMax) {
        return (value - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin;
    }

    /**
     * Calculate the length of a vector in 2D space.
     *
     * @param vec2 2D Vector.
     */
    public static double vectorLength2D(Vector2Df vec2) {
        return sqrt(vec2.x() * vec2.x() + vec2.y() * vec2.y());
    }

    /**
     * Normalize a vector in 2D space.
     *
     * @param vec2 2D Vector.
     */
    public static double[] normalize2D(Vector2Df vec2) {
        double vLen = vectorLength2D(vec2);

        return new double[] {vec2.x() / vLen, vec2.y() / vLen};
    }

    /**
     * Calculate the midpoint between two points in 2D space.
     *
     * @param point1 First point.
     * @param point2 Second point.
     */
    public static double[] midpoint2D(Vector2Df point1, Vector2Df point2) {
        return new double[] {(point1.x() + point2.x()) / 2, (point1.y() + point2.y()) / 2};
    }

    /**
     * Calculate the slope of a line given two points in 2D space.
     *
     * @param p1 First point.
     * @param p2 Second point.
     */
    public static double slope2D(Vector2Df p1, Vector2Df p2) {
        if (p1.x() == p2.x()) return -1;

        return (p2.y() - p1.y()) / (p2.x() - p1.x());
    }

    /**
     * Calculate the y-intercept of a line given its slope and a point on the line.
     *
     * @param point A point on the line.
     * @param slope The slope of the line.
     */
    public static double yIntercept(Vector2Df point, double slope) {
        return point.y() - slope * point.x();
    }

    /**
     * Calculate a linear interpolation between two values.
     *
     * @param start Starting value.
     * @param end Ending value.
     * @param iFac Interpolation factor.
     */
    public static double lerp(double start, double end, double iFac) {
        return start + iFac * (end - start);
    }

    /**
     * Calculate the reflection vector given an incident vector and a normal vector in 2D space.
     *
     * @param incident The incident vector.
     * @param normal The normal vector.
     */
    public static double[] reflectVector2D(Vector2Df incident, Vector2Df normal) {
        double dotProduct = dotProd3D(new Vector3Df(incident.x(), incident.y(), 0), new Vector3Df(normal.x(), normal.y(), 0));

        return new double[] {
            incident.x() - 2 * dotProduct * normal.x(),
            incident.y() - 2 * dotProduct * normal.y()
        };
    }

    /**
     * Check if a point is inside a rectangle in 2D space.
     *
     * @param point The point to check.
     * @param rectPos The position of the rectangle (bottom-left corner).
     * @param rectSize Rectangle size.
     */
    public static boolean isPointInRectangle(Vector2Df point, Vector2Df rectPos, Vector2Df rectSize) {
        return point.x() >= rectPos.x() && point.x() <= rectPos.x() + rectSize.x() &&
                point.y() >= rectPos.y() && point.y() <= rectPos.y() + rectSize.y();
    }

    /**
     * Check if two rectangles intersect in 2D space.
     *
     * @param fRectSize First rectangle size.
     * @param fRectPos First rectangle position.
     * @param sRectSize Second rectangle size.
     * @param sRectPos Second rectangle position.
     */
    public static boolean rectIntersects(Vector2Df fRectSize, Vector2Df fRectPos, Vector2Df sRectSize, Vector2Df sRectPos) {
        return fRectPos.x() < sRectPos.x() + sRectSize.x() && fRectPos.x() + fRectSize.x() > sRectPos.x() &&
                fRectPos.y() < sRectPos.y() + sRectSize.y() && fRectPos.y() + fRectSize.y() > sRectPos.y();
    }

    /**
     * Linearly interpolate between two vectors in 2D space.
     *
     * @param start The starting vector.
     * @param end The ending vector.
     * @param iFac Interpolation factor (0.0 to 1.0).
     */
    public static double[] lerp2D(Vector2Df start, Vector2Df end, double iFac) {
        return new double[] {
            lerp(start.x(), end.x(), iFac),
            lerp(start.y(), end.y(), iFac)
        };
    }

    /**
     * Rotate a vector around a point in 2D space.
     *
     * @param point The point around which to rotate.
     * @param vector The vector to rotate.
     * @param angle The angle in degrees to rotate the vector.
     */
    public static double[] rotateVectorAroundPoint2D(Vector2Df point, Vector2Df vector, double angle) {
        double[] translatedVec = new double[] {vector.x() - point.x(), vector.y() - point.y()};

        double[] rotatedVector = rotatePoint2D(new Vector2Df((float) translatedVec[0], (float) translatedVec[1]), angle);

        return new double[] {rotatedVector[0] + point.x(), rotatedVector[1] + point.y()};
    }

    /**
     * Summarize vector (array).
     *
     * @param vec Vector.
     */
    public static double sumVec(double[] vec) {
        return Arrays.stream(vec).sum();
    }

    /**
     * Subtract elements of two vectors.
     *
     * @param fVec First vector.
     * @param sVec Second vector.
     */
    public static double[] subVec(double[] fVec, double[] sVec) {
        if(fVec.length != sVec.length) return null;

        double[] result = new double[fVec.length];

        for(int i = 0; i < fVec.length; i++) result[i] = fVec[i] - sVec[i];

        return result;
    }

    /**
     * Multiply elements of two vectors.
     *
     * @param fVec First vector.
     * @param sVec Second vector.
     */
    public static double[] multVec(double[] fVec, double[] sVec) {
        if(fVec.length != sVec.length) return null;

        double[] result = new double[fVec.length];

        for(int i = 0; i < fVec.length; i++) result[i] = fVec[i] * sVec[i];

        return result;
    }

    /**
     * Divide elements of two vectors.
     *
     * @param fVec First vector.
     * @param sVec Second vector.
     */
    public static double[] divVec(double[] fVec, double[] sVec) {
        if(fVec.length != sVec.length) return null;

        double[] result = new double[fVec.length];

        for(int i = 0; i < fVec.length; i++) result[i] = fVec[i] / sVec[i];

        return result;
    }

    /**
     * Scale value from range to other range (I.E scaleRange(99, 75, 100) -> 74.25).
     *
     * @param value Value to scale.
     * @param maxValue Max possible value.
     * @param scale Scale.
     */
    public static double scale(double value, double maxValue, double scale) {
        return ((value / scale) * maxValue);
    }

    /**
     * Rotate 2D point around its origin.
     *
     * @param x Point X.
     * @param y Point Y.
     * @param originX Origin X.
     * @param originY Origin Y.
     * @param angle Angle.
     */
    public static double[] rotate2DPoint(double x, double y, double originX, double originY, double angle) {
        double radians = Math.toRadians(angle);

        return new double[] {(x - originX) * Math.cos(radians) - (y - originY) * Math.sin(radians), (x - originX) * Math.sin(radians) + (y - originY) * Math.cos(radians)};
    }

    /**
     * Checks whether value exceed or underestimate the maximum target threshold.
     *
     * @param value Value.
     * @param target Target.
     * @param threshold Threshold.
     */
    public static boolean tolerance(double value, double target, double threshold) {
        return Math.abs(value - target) <= threshold;
    }

    /**
     * Checks whether value(s) exceed or underestimate the maximum target threshold.
     * Returns false if the length of values is not equal to the length of target or the length of threshold is not equal to the length of values.
     *
     * @param values Values.
     * @param target Target.
     * @param threshold Threshold.
     */
    public static boolean tolerance(double[] values, double[] target, double[] threshold) {
        if(values.length != target.length || threshold.length != values.length) return false;

        for(int i=0; i < values.length; i++) {
            if(!tolerance(values[i], target[i], threshold[i])) return false;
        }

        return true;
    }

    /**
     * Calculate the average of an array of numbers.
     *
     * @param numbers Array of numbers.
     */
    public static double average(double[] numbers) {
        return sumVec(numbers) / numbers.length;
    }

    /**
     * Calculate the median of an array of numbers.
     *
     * @param numbers Array of numbers.
     */
    public static double median(double[] numbers) {
        double[] sorted = Arrays.copyOf(numbers, numbers.length); Arrays.sort(sorted);

        int middle = sorted.length / 2;

        if(sorted.length % 2 == 0) {
            return (sorted[middle - 1] + sorted[middle]) / 2.0;
        } else {
            return sorted[middle];
        }
    }

    /**
     * Calculate the variance of an array of numbers.
     *
     * @param numbers Array of numbers.
     */
    public static double variance(double[] numbers) {
        double mean = average(numbers);

        double temp = 0;

        for(double num : numbers) temp += (num - mean) * (num - mean);

        return temp / (numbers.length - 1);
    }

    /**
     * Calculate the standard deviation of an array of numbers.
     *
     * @param numbers Array of numbers.
     */
    public static double standardDeviation(double[] numbers) {
        return Math.sqrt(variance(numbers));
    }

    /**
     * Find the maximum value in an array of numbers.
     *
     * @param numbers Array of numbers.
     */
    public static double max(double[] numbers) {
        double maxVal = Double.NEGATIVE_INFINITY;

        for(double num : numbers) {
            if(num > maxVal) maxVal = num;
        }

        return maxVal;
    }

    /**
     * Find the minimum value in an array of numbers.
     *
     * @param numbers Array of numbers.
     */
    public static double min(double[] numbers) {
        double minVal = Double.POSITIVE_INFINITY;

        for(double num : numbers) {
            if(num < minVal) minVal = num;
        }

        return minVal;
    }

    /**
     * Calculate the factorial of a number.
     *
     * @param num Number.
     */
    public static long factorial(int num) {
        if(num < 0) return 0;

        long result = 1;

        for(int i = 1; i <= num; i++) result *= i;

        return result;
    }

    /**
     * Calculate the greatest common divisor (GCD) of two numbers.
     *
     * @param fNum First number.
     * @param sNum Second number.
     */
    public static int gcd(int fNum, int sNum) {
        while(sNum != 0) {
            int temp = sNum;

            sNum = fNum % sNum;
            fNum = temp;
        }

        return fNum;
    }

    /**
     * Calculate the least common multiple (LCM) of two numbers.
     *
     * @param fNum First number.
     * @param sNum Second number.
     */
    public static int lcm(int fNum, int sNum) {
        return Math.abs(fNum * sNum) / gcd(fNum, sNum);
    }

    /**
     * Calculate the n-th Fibonacci number.
     *
     * @param num The position in the Fibonacci sequence.
     */
    public static long fibonacci(int num) {
        if(num <= 0) return 0;

        if(num == 1 || num == 2) return 1;

        long a = 1, b = 1;

        for(int i = 3; i <= num; i++) {
            long next = a + b;

            a = b;
            b = next;
        }

        return b;
    }
}
