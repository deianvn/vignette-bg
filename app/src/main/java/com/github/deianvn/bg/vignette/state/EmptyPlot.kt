package com.github.deianvn.bg.vignette.state

class EmptyPlot : Plot<EmptyPlot> {
    override fun copyObject(): EmptyPlot {
        return this
    }
}
