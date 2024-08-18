package br.alexandregpereira.hunter.folder.insert

import br.alexandregpereira.hunter.localization.Language

interface FolderInsertStrings {
    val addToFolder: String
    val folderNameLabel: String
    val save: String
    val share: String
}

internal data class FolderInsertEnStrings(
    override val addToFolder: String = "Add to folder",
    override val folderNameLabel: String = "Folder name",
    override val save: String = "Save",
    override val share: String = "Share",
) : FolderInsertStrings

internal data class FolderInsertPtStrings(
    override val addToFolder: String = "Adicionar à pasta",
    override val folderNameLabel: String = "Nome da pasta",
    override val save: String = "Salvar",
    override val share: String = "Compartilhar",
) : FolderInsertStrings

internal data class FolderInsertEmptyStrings(
    override val addToFolder: String = "",
    override val folderNameLabel: String = "",
    override val save: String = "",
    override val share: String = "",
) : FolderInsertStrings

internal fun getFolderInsertStrings(lang: Language): FolderInsertStrings {
    return when (lang) {
        Language.ENGLISH -> FolderInsertEnStrings()
        Language.PORTUGUESE -> FolderInsertPtStrings()
    }
}
