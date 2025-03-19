package com.spldev.hiraganamaster.data.datasource

object KatakanaData {
    val katakanaList = listOf(
        // Vocales
        Pair("ア", "a"),
        Pair("イ", "i"),
        Pair("ウ", "u"),
        Pair("エ", "e"),
        Pair("オ", "o"),

        // K-column
        Pair("カ", "ka"),
        Pair("キ", "ki"),
        Pair("ク", "ku"),
        Pair("ケ", "ke"),
        Pair("コ", "ko"),

        // S-column
        Pair("サ", "sa"),
        Pair("シ", "shi"),
        Pair("ス", "su"),
        Pair("セ", "se"),
        Pair("ソ", "so"),

        // T-column
        Pair("タ", "ta"),
        Pair("チ", "chi"),
        Pair("ツ", "tsu"),
        Pair("テ", "te"),
        Pair("ト", "to"),

        // N-column
        Pair("ナ", "na"),
        Pair("ニ", "ni"),
        Pair("ヌ", "nu"),
        Pair("ネ", "ne"),
        Pair("ノ", "no"),

        // H-column
        Pair("ハ", "ha"),
        Pair("ヒ", "hi"),
        Pair("フ", "fu"),
        Pair("ヘ", "he"),
        Pair("ホ", "ho"),

        // M-column
        Pair("マ", "ma"),
        Pair("ミ", "mi"),
        Pair("ム", "mu"),
        Pair("メ", "me"),
        Pair("モ", "mo"),

        // Y-column
        Pair("ヤ", "ya"),
        Pair("ユ", "yu"),
        Pair("ヨ", "yo"),

        // R-column
        Pair("ラ", "ra"),
        Pair("リ", "ri"),
        Pair("ル", "ru"),
        Pair("レ", "re"),
        Pair("ロ", "ro"),

        // W-column
        Pair("ワ", "wa"),
        Pair("ヲ", "wo"),

        // N (special character)
        Pair("ン", "n")
    )

    fun getAllKatakanas(): List<Pair<String, String>> {
        return katakanaList
    }
}