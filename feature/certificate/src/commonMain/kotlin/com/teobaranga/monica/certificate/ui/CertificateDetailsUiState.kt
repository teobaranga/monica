package com.teobaranga.monica.certificate.ui

data class CertificateDetailsUiState(
    val sections: List<Section>,
) {
    data class Section(
        val title: String,
        val items: List<Item>,
    ) {
        data class Item(
            val title: String,
            val value: String,
            val isCopyEnabled: Boolean = false,
        )
    }
}
