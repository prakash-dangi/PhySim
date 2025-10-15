package Physics.Waves;

public class Wave {
    float maxAmplitude;
    float k, w;
    float phi;
    float instant;
    float x, y;

    Wave(float amplitude, float k, float instant, float w, float phi) {
        this.maxAmplitude = amplitude;
        this.k = k;
        this.instant = instant;
        this.w = w;
        this.phi = phi;
    }

    float value(float x) {
        return (float)(maxAmplitude*Math.sin(k*x + w*instant + phi));
    }
}
