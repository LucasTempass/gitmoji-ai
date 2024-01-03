package com.cerveira.lucas.gitmoji.data

import com.google.gson.Gson

class Gitmoji(
    val value: String,
    val code: String,
    val description: String,
    val name: String,
    val embedding: List<Double> = listOf()
)

val gitmojis: List<Gitmoji> = listOf(
    Gitmoji("✨", ":sparkles:", "Introduce new features.", "sparkles", listOf(0.0)),
    Gitmoji("🐛", ":bug:", "Fix a bug.", "bug"),
    Gitmoji("🎨", ":art:", "Improve structure/format of the code.", "art"),
    Gitmoji("💄", ":lipstick:", "Add or update the UI and style files.", "lipstick"),
    Gitmoji("🔥", ":fire:", "Remove code or files.", "fire"),
    Gitmoji("🚑️", ":ambulance:", "Critical hotfix.", "ambulance"),
    Gitmoji("♻️", ":recycle:", "Refactor code.", "recycle"),
    Gitmoji("🧪", ":test_tube:", "Add a failing test.", "test-tube"),
    Gitmoji("✅", ":white_check_mark:", "Add, update, or pass tests.", "white-check-mark"),
    Gitmoji("📝", ":memo:", "Add or update documentation.", "memo"),
    Gitmoji("🔒️", ":lock:", "Fix security issues.", "lock"),
    Gitmoji("🚨", ":rotating_light:", "Fix compiler / linter warnings.", "rotating-light"),
    Gitmoji("🚧", ":construction:", "Work in progress.", "construction"),
    Gitmoji("💡", ":bulb:", "Add or update comments in source code.", "bulb"),
    Gitmoji("🚚", ":truck:", "Move or rename resources (e.g.: files, paths, routes).", "truck"),
    Gitmoji("➕", ":heavy_plus_sign:", "Add a dependency.", "heavy-plus-sign"),
    Gitmoji("➖", ":heavy_minus_sign:", "Remove a dependency.", "heavy-minus-sign"),
    Gitmoji("⬆️", ":arrow_up:", "Upgrade dependencies.", "arrow-up"),
    Gitmoji("⬇️", ":arrow_down:", "Downgrade dependencies.", "arrow-down"),
    Gitmoji("✏️", ":pencil2:", "Fix typos.", "pencil2"),
    Gitmoji("🚀", ":rocket:", "Deploy stuff.", "rocket"),
    Gitmoji("🎉", ":tada:", "Begin a project.", "tada"),
    Gitmoji("⚰️", ":coffin:", "Remove dead code.", "coffin"),
    Gitmoji("👷", ":construction_worker:", "Add or update CI build system.", "construction-worker"),
    Gitmoji("📌", ":pushpin:", "Pin dependencies to specific versions.", "pushpin"),
    Gitmoji("📈", ":chart_with_upwards_trend:", "Add or update analytics or track code.", "chart-with-upwards-trend"),
    Gitmoji("🔧", ":wrench:", "Add or update configuration files.", "wrench"),
    Gitmoji("🔨", ":hammer:", "Add or update development scripts.", "hammer"),
    Gitmoji("🌐", ":globe_with_meridians:", "Internationalization and localization.", "globe-with-meridians"),
    Gitmoji("💚", ":green_heart:", "Fix CI Build.", "green-heart"),
    Gitmoji("⚡️", ":zap:", "Improve performance.", "zap"),
    Gitmoji("💩", ":poop:", "Write bad code that needs to be improved.", "poop"),
    Gitmoji("⏪️", ":rewind:", "Revert changes.", "rewind"),
    Gitmoji("🔀", ":twisted_rightwards_arrows:", "Merge branches.", "twisted-rightwards-arrows"),
    Gitmoji("📦️", ":package:", "Add or update compiled files or packages.", "package"),
    Gitmoji("🔐", ":closed_lock_with_key:", "Add or update secrets.", "closed-lock-with-key"),
    Gitmoji("👽️", ":alien:", "Update code due to external API changes.", "alien"),
    Gitmoji("📄", ":page_facing_up:", "Add or update license.", "page-facing-up"),
    Gitmoji("🔖", ":bookmark:", "Release / Version tags.", "bookmark"),
    Gitmoji("💥", ":boom:", "Introduce breaking changes.", "boom"),
    Gitmoji("🍱", ":bento:", "Add or update assets.", "bento"),
    Gitmoji("♿️", ":wheelchair:", "Improve accessibility.", "wheelchair"),
    Gitmoji("🍻", ":beers:", "Write code drunkenly.", "beers"),
    Gitmoji("💬", ":speech_balloon:", "Add or update text and literals.", "speech-balloon"),
    Gitmoji("🗃️", ":card_file_box:", "Perform database related changes.", "card-file-box"),
    Gitmoji("🔊", ":loud_sound:", "Add or update logs.", "loud-sound"),
    Gitmoji("🔇", ":mute:", "Remove logs.", "mute"),
    Gitmoji("👥", ":busts_in_silhouette:", "Add or update contributor(s).", "busts-in-silhouette"),
    Gitmoji("🚸", ":children_crossing:", "Improve user experience / usability.", "children-crossing"),
    Gitmoji("🏗️", ":building_construction:", "Make architectural changes.", "building-construction"),
    Gitmoji("📱", ":iphone:", "Work on responsive design.", "iphone"),
    Gitmoji("🤡", ":clown_face:", "Mock things.", "clown-face"),
    Gitmoji("🥚", ":egg:", "Add or update an easter egg.", "egg"),
    Gitmoji("🙈", ":see_no_evil:", "Add or update a .gitignore file.", "see-no-evil"),
    Gitmoji("📸", ":camera_flash:", "Add or update snapshots.", "camera-flash"),
    Gitmoji("⚗️", ":alembic:", "Perform experiments.", "alembic"),
    Gitmoji("🔍️", ":mag:", "Improve SEO.", "mag"),
    Gitmoji("🏷️", ":label:", "Add or update types.", "label"),
    Gitmoji("🌱", ":seedling:", "Add or update seed files.", "seedling"),
    Gitmoji("🚩", ":triangular_flag_on_post:", "Add, update, or remove feature flags.", "triangular-flag-on-post"),
    Gitmoji("🥅", ":goal_net:", "Catch errors.", "goal-net"),
    Gitmoji("💫", ":dizzy:", "Add or update animations and transitions.", "animation"),
    Gitmoji("🗑️", ":wastebasket:", "Deprecate code that needs to be cleaned up.", "wastebasket"),
    Gitmoji("🩹", ":adhesive_bandage:", "Simple fix for a non-critical issue.", "adhesive-bandage"),
    Gitmoji("🧐", ":monocle_face:", "Data exploration/inspection.", "monocle-face"),
    Gitmoji("👔", ":necktie:", "Add or update business logic.", "necktie"),
    Gitmoji("🩺", ":stethoscope:", "Add or update healthcheck.", "stethoscope"),
    Gitmoji("🧱", ":bricks:", "Infrastructure related changes.", "bricks"),
    Gitmoji("🧑‍💻", ":technologist:", "Improve developer experience.", "technologist"),
    Gitmoji("💸", ":money_with_wings:", "Add sponsorships or money related infrastructure.", "money-with-wings"),
    Gitmoji("🧵", ":thread:", "Add or update code related to multithreading or concurrency.", "thread"),
    Gitmoji("🦺", ":safety_vest:", "Add or update code related to validation.", "safety-vest"),
    Gitmoji(
        "🛂", ":passport_control:", "Work on code related to authorization, roles and permissions.", "passport-control"
    )
)


class Gitmojis {

    var gitmojis: List<Gitmoji> = listOf()

    data class GitmojiData(
        val gitmojis: List<Gitmoji>
    )

    init {
        loadDefaultGitmoji();
    }

    private fun loadDefaultGitmoji() {
        javaClass.getResourceAsStream("/data/gitmojis.json").use { inputStream ->
            if (inputStream != null) {
                val text = inputStream.bufferedReader().readText()
                gitmojis = run {
                    val data = Gson().fromJson(text, GitmojiData::class.java)
                    data.gitmojis
                }
            }
        }
    }

}

