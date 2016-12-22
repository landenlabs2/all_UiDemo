#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(com.landenlabs.all_UiDemo.Util)

#include "rs_graphics.rsh"

// Tint image

// Following gXXXX are set by calling java code.

rs_allocation gIn;
rs_allocation gOut;
rs_script gScript;

// Tint color channels.
float gRed;
float gGreen;
float gBlue;
float gAlpha;


void root(const uchar4 *v_in, uchar4 *v_out, const void *usrData, uint32_t x, uint32_t y) {
    float4 in4 = rsUnpackColor8888(*v_in);

    float3  tint3 =  {gRed, gGreen, gBlue};
    float inAlpha = 1 - gAlpha;
    float3 out3 = in4.rgb * inAlpha + tint3.rgb * gAlpha;
 
    *v_out = rsPackColorTo8888(out3);
}

void filter() {
    rsForEach(gScript, gIn, gOut);
}