package com.spldev.hiraganamaster.datasource

object HiraganaData {
    val hiraganaList = listOf(
        // Vocales
        Pair("あ", "a"),
        Pair("い", "i"),
        Pair("う", "u"),
        Pair("え", "e"),
        Pair("お", "o"),

        // K-column
        Pair("か", "ka"),
        Pair("き", "ki"),
        Pair("く", "ku"),
        Pair("け", "ke"),
        Pair("こ", "ko"),

        // S-column
        Pair("さ", "sa"),
        Pair("し", "shi"),
        Pair("す", "su"),
        Pair("せ", "se"),
        Pair("そ", "so"),

        // T-column
        Pair("た", "ta"),
        Pair("ち", "chi"),
        Pair("つ", "tsu"),
        Pair("て", "te"),
        Pair("と", "to"),

        // N-column
        Pair("な", "na"),
        Pair("に", "ni"),
        Pair("ぬ", "nu"),
        Pair("ね", "ne"),
        Pair("の", "no"),

        // H-column
        Pair("は", "ha"),
        Pair("ひ", "hi"),
        Pair("ふ", "fu"),
        Pair("へ", "he"),
        Pair("ほ", "ho"),

        // M-column
        Pair("ま", "ma"),
        Pair("み", "mi"),
        Pair("む", "mu"),
        Pair("め", "me"),
        Pair("も", "mo"),

        // Y-column
        Pair("や", "ya"),
        Pair("ゆ", "yu"),
        Pair("よ", "yo"),

        // R-column
        Pair("ら", "ra"),
        Pair("り", "ri"),
        Pair("る", "ru"),
        Pair("れ", "re"),
        Pair("ろ", "ro"),

        // W-column
        Pair("わ", "wa"),
        Pair("を", "wo"),

        // N (special character)
        Pair("ん", "n")
    )

    fun getallHiraganas(): List<Pair<String, String>> {
        return hiraganaList
    }
}