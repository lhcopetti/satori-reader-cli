package com.copetti.core.usecase

import com.copetti.model.SatoriReaderStatus

class GetProgressStatusMarker {

    fun get(status: SatoriReaderStatus ) = when(status) {
        SatoriReaderStatus.UNREAD ->"![#f03c15](https://placehold.co/15x15/f03c15/.png?text=.)"
        SatoriReaderStatus.STARTED ->"![#f03c15](https://placehold.co/15x15/f03c15/.png?text=.)"
        SatoriReaderStatus.COMPLETED ->"![#00ff00](https://placehold.co/15x15/00ff00/.png?text=.)"
    }

}