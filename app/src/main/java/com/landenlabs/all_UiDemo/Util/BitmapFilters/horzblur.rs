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

// ---- Horizontal Blur ----

// User provided parameters
int Radius = 8;
const uchar4* inPtr;
uchar4* outPtr;
rs_allocation inImage;
rs_allocation outImage;


// Internal  values
int numSamples;
float invN;    

uint32_t xDim;
uint32_t yDim;  

void init_calc() {

    xDim = rsAllocationGetDimX(inImage);
    yDim = rsAllocationGetDimY(inImage);
    inPtr = (const uchar4*)rsGetElementAt(inImage, 0, 0);
    outPtr = (uchar4*)rsGetElementAt(outImage, 0, 0);

    if (Radius*2 >= xDim) 
        Radius = (xDim - 1)/2;
        
    numSamples = 2 * Radius + 1;
    invN = 1.0f / numSamples;   
}    

void root(const ushort* pRowIndex) {
 
    const uchar4* head = inPtr + *pRowIndex * xDim;
    const uchar4* tail = head;
    uchar4* pOut = outPtr + *pRowIndex * xDim;
    
    // Split row into three parts:
    // Radius = 7
    //  
    //     Left Margin       Middle        Right Margin
    // |------>R=======------------------======>R-------|
    // |123456787654321..................123456787654321| 
    //
    
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

}