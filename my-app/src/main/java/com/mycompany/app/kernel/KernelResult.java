package com.mycompany.app.kernel;

public record KernelResult(
    int TotalAccesses,
    int PageFault,
    double succesTime
) {}

