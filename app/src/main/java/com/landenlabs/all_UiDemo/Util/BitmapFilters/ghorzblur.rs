#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(com.landenlabs.all_UiDemo.Util)

/**
 * Copyright (c) 2015 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Dennis Lang  (3/21/2015)
 * @see http://landenlabs.com
 *
 */

#include "rs_graphics.rsh"

// ---- Gauassian Horizontal Blur ----

// User provided parameters
int Radius = 8;
const uchar4* inPtr;
uchar4* outPtr;
rs_allocation inImage;
rs_allocation outImage;

// Internal  values
uint32_t xDim;
uint32_t yDim;
uint32_t inLen;

void init_calc() {

    xDim = rsAllocationGetDimX(inImage);
    yDim = rsAllocationGetDimY(inImage);
    inPtr = (const uchar4*)rsGetElementAt(inImage, 0, 0);
    outPtr = (uchar4*)rsGetElementAt(outImage, 0, 0);

    if (Radius*2 >= xDim)
        Radius = (xDim - 1)/2;
}


/**
 * Trailing region behind core blur.
 * Left of core blur.
 * |(---TrailerPartial---)(---CenterFull---)(---LeaderPartial---)|
 * @param inData
 * @param outData
 */
static void blurTrailer(const uchar4* inData, uchar4* outData, int inLen)
{
    int endIdx = Radius - 1;

    float4 leader = 0;
    float4 sum = 0;
    float4 trailer = 0;
    int weight = Radius;

    for (int idx = 0; idx < Radius; idx++) {
        float4 pt = rsUnpackColor8888(inData[idx]);
        leader += pt;           // [0] + [1] + [2] + [3]
        sum += pt * weight;     // 4[0] + 3[1] + 2[2] + 1[3]
        weight--;
    }

    // [0] =  ( 4[0] + 3[1] + 2[2] + 1[3]               ) / 4+3+2+1
    // [1] =  ( 3[0] + 4[1] + 3[2] + 2[3] + 1[4]        ) / 3+4+3+2+1
    // [2] =  ( 2[0] + 3[1] + 4[2] + 3[3] + 2[4] + 1[5] ) / 2+3+4+3+2+1
    weight = (int)((Radius+1)*(Radius/2.0f));
    for (int idx = 0; idx < endIdx; idx++) {
        outData[idx] = rsPackColorTo8888(sum / weight);
        weight += endIdx-idx;
        trailer += rsUnpackColor8888(inData[idx]);             // [0]
        leader = leader - rsUnpackColor8888(inData[idx]) + rsUnpackColor8888(inData[Radius + idx]);     // [1] + [2] + [3] + [4]
        sum = sum - trailer + leader;
    }
}

/**
 * Leading blur region in front of core blur
 * Right of core blur
 * |(---TrailerPartial---)(---CenterFull---)(---LeaderPartial---)|
 * @param inData
 * @param outData
 */
static void blurLeader(const uchar4* inData, uchar4* outData, int inLen)
{
    float4 leader = 0;
    float4 sum = 0;
    float4 trailer = 0;
    int weight = 0;
    int idx = inLen - Radius*2 + 2;

    while (weight++ < Radius) {
        float4 pt = rsUnpackColor8888(inData[idx++]);
        trailer += pt;
        sum += pt * weight;
    }
    //  0..19 [20]  Radius=6         10 11 12 13 14 15 16 17 18 19
    //                                1  2  3  4  5  6  5  4  3  2
    //                               10 15 10 15 10 15 10 15 10 15

    // mSum = 1*10 + 2*15 + 3*10 + 4*15 + 5*10 + 6*15 =  90+12*15 = 90+150+30 = 270
    // mTrailer = 10+15+10+15+10+15  = 30+45=75

    weight = Radius;
    leader = 0;
    while (idx < inLen) {
        weight--;
        float4 pt = rsUnpackColor8888(inData[idx++]);
        leader += pt;
        sum += pt * weight;
    }

    // mSum = 270 + 5*10 + 4*15 + 3*10 + 2*15 = 270 + 80 + 90 = 440
    // mLeader = 10 + 15 + 10 + 15  = 50
    // mWeight = 35

    weight = Radius * Radius - 1;
    // Radius = 4, length=8
    // [4] = ( 1[1] + 2[2] + 3[3] + 4[4] + 3[5] + 2[6] + 1[7] ) / 1+2+3+4+3+2+1
    //... start partial mTrailer processing
    // [5] = (        1[2] + 2[3] + 3[4] + 4[5] + 3[6] + 2[7] ) / 1+2+3+4+3+2
    // [6] = (               1[3] + 2[4] + 3[5] + 4[6] + 3[7] ) / 1+2+3+4+3
    // [7] = (                      1[4] + 2[5] + 3[6] + 4[7] ) / 1+2+3+4
    int off = 2;
    for (idx = inLen - Radius +1; idx < inLen; idx++) {
        outData[idx] = rsPackColorTo8888(sum / weight);

        sum = sum - trailer + leader;
        weight -= off++;
        trailer = trailer - rsUnpackColor8888(inData[idx-Radius+1]) + rsUnpackColor8888(inData[idx]);
        leader = leader - rsUnpackColor8888(inData[idx]);
    }
}



/**
 * |(---TrailerPartial---)(---CenterFull---)(---LeaderPartial---)|
 * @param inData
 * @param outData
 */
static void blurCenter(const uchar4* inData, uchar4* outData, int inLen)
{
    float4 sum = 0;
    float4 trailer = 0;
    int idx = 0;
    while (idx < Radius) {
        float4 pt = rsUnpackColor8888(inData[idx++]);
        trailer += pt;      // [0] + [1] + [2] + [3]
        sum += pt * idx;    // 1[0] + 2[1] + 3[2] + 4[3]  ex Radius=4
    }

    float4 leader = 0;
    int weight = Radius - 1;
    while (weight > 0) {
        float4 pt = rsUnpackColor8888(inData[idx++]);
        leader += pt;       // [4] + [5] + [6]
        sum += pt * weight; // 3[4] + 2[5] + 1[6]  ex Radius=4
        weight--;
    }

    weight = Radius * Radius;
    // mSum =  1[0] + 2[1] + 3[2] + 4[3] + 3[4] + 2[5] + 1[6]  ex Radius=4

    int endIdx = inLen;

    int trailIdx = 0;                   // 0
    int centerIdx = Radius-1;           // 3
    int leadIdx = (Radius * 2) -1;      // 7
    float4 error = 0;

    while (leadIdx < endIdx) {
        float4 avg = (sum + error) / weight;
        error += sum - (avg * weight);
        outData[centerIdx++] = rsPackColorTo8888(avg);

        float4 trail = rsUnpackColor8888(inData[trailIdx++]);             // [0]
        float4 center = rsUnpackColor8888(inData[centerIdx]);             // [4]
        float4 lead = rsUnpackColor8888(inData[leadIdx++]);               // [7]

        // Radius = 4
        //  before =  1*[0] + 2[1] + 3[2] + 4[r] + 3[4] + 2[5] + 1[6]
        //  after  =          1[1] + 2[2] + 3[3] + 4[4] + 3[5] + 2[6] + 1[7]
        sum     = sum - trailer + leader + lead;
        trailer = trailer - trail + center;     // ([0] + [1] + [2] + [3]) -[0] + [4]  = [1] + [2] + [3] + [4]
        leader  = leader - center +  lead;      // ([4] + [5] + [6]) - [4] + [7] = [5] + [6] + [7]
    }

    outData[centerIdx] = rsPackColorTo8888(sum / weight);
}


void root(const ushort* pRowIndex) {

    const uchar4* head = inPtr + *pRowIndex * xDim;
    uchar4* pOut = outPtr + *pRowIndex * xDim;

    // Split row into three parts:
    // Radius = 7
    //
    //     Trailer           Center        Leader
    // |------>R=======------------------======>R-------|
    // |123456787654321..................123456787654321|
    //

    blurTrailer(inPtr, outPtr, xDim);
    blurCenter(inPtr, outPtr, xDim);
    blurLeader(inPtr, outPtr, xDim);

/*
    const uchar4* leftMargin = head + Radius;
    const uchar4* leftMiddle = head + numSamples;
    const uchar4* rightMiddle = head + xDim;
    const uchar4* rightMargin = head + xDim - Radius - 1;

    float4 sum = 0;

    // Accumulate left edge in margin
    while (head < leftMargin) {
        sum += rsUnpackColor8888(*head++);
    }

    // Compute left half margin output
    int cntSum = Radius;
    while (head < leftMiddle) {
        sum += rsUnpackColor8888(*head++);
        *pOut++ = rsPackColorTo8888(sum / ++cntSum);
    }

    // Compute output in middle where full samples available.
    while (head < rightMiddle) {
        sum += rsUnpackColor8888(*head++);
        sum -= rsUnpackColor8888(*tail++);
        *pOut++ = rsPackColorTo8888(sum * invN);
    }

    // Compute right half margin output
    while (tail < rightMargin) {
        sum -= rsUnpackColor8888(*tail++);
        *pOut++ = rsPackColorTo8888(sum / --cntSum);
    }
    */
}
