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

// ---- Vertical Blur by Dennis Lang ----

// User provided parameters
int Radius = 8;
const uchar4* inPtr;
uchar4* outPtr;
rs_allocation inImage;
rs_allocation outImage;

// Internval values
int numSamples;
float invN;    

uint32_t xDim;
uint32_t yDim;    

void init_calc() {
    xDim = rsAllocationGetDimX(inImage);
    yDim = rsAllocationGetDimY(inImage);
    inPtr = (const uchar4*)rsGetElementAt(inImage, 0, 0);
    outPtr = (uchar4*)rsGetElementAt(outImage, 0, 0);
    
    if (Radius*2 >= yDim) 
        Radius = (yDim - 1)/2;
        
    numSamples = 2 * Radius + 1;
    invN = 1.0f / numSamples;    
}    

void root(const ushort* pColumIndex) {
    
    const uchar4* head = inPtr + *pColumIndex;
    const uchar4* tail = head;
    uchar4* pOut = outPtr + *pColumIndex;
        
    const uchar4* topMargin = head + xDim * Radius;
    const uchar4* begMiddle = head + xDim * numSamples;
    const uchar4* endMiddle = head + xDim * yDim;
    const uchar4* botMargin = head + xDim * (yDim - Radius -1);
    
    float4 sum = 0;    
    // Accumulate top margin
    while (head < topMargin) {
        sum += rsUnpackColor8888(*head);
        head += xDim;
    }    
      
    // Output top half margin
    int cntSum = Radius;
    while (head < begMiddle) {
        sum += rsUnpackColor8888(*head);
        *pOut = rsPackColorTo8888(sum / ++cntSum);    
        head += xDim;
        pOut += xDim;
    }    
    
    // Output middle with full samples
    while (head < endMiddle) {
        sum += rsUnpackColor8888(*head);
        sum -= rsUnpackColor8888(*tail);
        *pOut = rsPackColorTo8888(sum*invN);
        head += xDim;
        tail += xDim;
        pOut += xDim;
    }    
    
    // Output bottom half margin.
    while (tail < botMargin) {
        sum -= rsUnpackColor8888(*tail);
        *pOut = rsPackColorTo8888(sum / --cntSum);
        tail += xDim;
        pOut += xDim;
    }
}