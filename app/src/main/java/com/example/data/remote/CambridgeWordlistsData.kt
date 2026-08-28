package com.example.data.remote

/**
 * Official Cambridge English Wordlists Catalog
 * Reference: Cambridge Pre A1 Starters, A1 Movers and A2 Flyers Wordlists
 * Includes 18 Thematic Categories, Grammatical Classification, and Examination Levels.
 */

enum class CambridgeYleLevel(val code: String, val title: String, val cefr: String, val badgeColorHex: Long) {
    STARTERS("S", "Pre A1 Starters", "Pre A1", 0xFF00897B),
    MOVERS("M", "A1 Movers", "A1", 0xFF1E88E5),
    FLYERS("F", "A2 Flyers", "A2", 0xFFE65100)
}

data class CambridgeVocabItem(
    val english: String,
    val spanish: String,
    val phonetic: String,
    val level: CambridgeYleLevel,
    val partOfSpeech: String, // noun, verb, adj, adv, prep, etc.
    val theme: String,
    val exampleEnglish: String,
    val exampleSpanish: String,
    val isIrregularVerb: Boolean = false
)

object CambridgeWordlistsData {

    val thematicCategories = listOf(
        "Todos los Temas",
        "Animals (Animales)",
        "The body and the face (Cuerpo y Cara)",
        "Clothes (Ropa y Accesorios)",
        "Colours (Colores)",
        "Family & friends (Familia y Amigos)",
        "Food & drink (Comida y Bebida)",
        "Health (Salud)",
        "The home (El Hogar)",
        "Materials (Materiales)",
        "Places & directions (Lugares y Direcciones)",
        "School (Escuela y Educación)",
        "Sports & leisure (Deportes y Ocio)",
        "Time (Tiempo y Fechas)",
        "Toys (Juguetes)",
        "Transport (Transporte)",
        "Weather (Clima)",
        "Work (Trabajo y Profesiones)",
        "The world around us (El Mundo que nos Rodea)"
    )

    val grammaticalCategories = listOf(
        "Todos",
        "Nouns (Sustantivos)",
        "Adjectives (Adjetivos)",
        "Verbs irregular (Verbos Irregulares)",
        "Verbs regular (Verbos Regulares)",
        "Modals (Verbos Modales)",
        "Adverbs (Adverbios)",
        "Prepositions (Preposiciones)",
        "Conjunctions (Conjunciones)",
        "Pronouns (Pronombres)",
        "Question words (Palabras Interrogativas)"
    )

    /**
     * Official Cambridge Wordlist across Pre A1 Starters, A1 Movers, A2 Flyers
     */
    val fullWordlist: List<CambridgeVocabItem> = listOf(
        // ================= 1. ANIMALS (ANIMALES) =================
        // Starters (Pre A1)
        CambridgeVocabItem("animal", "animal", "/ˈæn.ɪ.məl/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "A tiger is a wild animal.", "Un tigre es un animal salvaje."),
        CambridgeVocabItem("bear", "oso", "/beər/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The brown bear is sleeping in the cave.", "El oso pardo está durmiendo en la cueva."),
        CambridgeVocabItem("bee", "abeja", "/biː/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The bee is flying from flower to flower.", "La abeja está volando de flor en flor."),
        CambridgeVocabItem("bird", "pájaro / ave", "/bɜːd/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "A blue bird is singing in the tree.", "Un pájaro azul está cantando en el árbol."),
        CambridgeVocabItem("cat", "gato", "/kæt/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The little cat is sleeping on the mat.", "El gato pequeño está durmiendo en la alfombra."),
        CambridgeVocabItem("chicken", "pollo / gallina", "/ˈtʃɪk.ɪn/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "We have five chickens on our small farm.", "Tenemos cinco gallinas en nuestra pequeña granja."),
        CambridgeVocabItem("cow", "vaca", "/kaʊ/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The black and white cow eats fresh grass.", "La vaca blanca y negra come hierba fresca."),
        CambridgeVocabItem("crocodile", "cocodrilo", "/ˈkrɒk.ə.daɪl/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The big crocodile is swimming in the river.", "El gran cocodrilo está nadando en el río."),
        CambridgeVocabItem("dog", "perro", "/dɒɡ/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The friendly dog wags its tail.", "El perro amistoso mueve su cola."),
        CambridgeVocabItem("donkey", "burro", "/ˈdɒŋ.ki/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The donkey is carrying two heavy baskets.", "El burro lleva dos canastas pesadas."),
        CambridgeVocabItem("duck", "pato", "/dʌk/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "Three yellow ducks are swimming in the pond.", "Tres patos amarillos están nadando en el estanque."),
        CambridgeVocabItem("elephant", "elefante", "/ˈel.ɪ.fənt/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "An elephant has a long grey trunk.", "Un elefante tiene una larga trompa gris."),
        CambridgeVocabItem("fish", "pez / pescado", "/fɪʃ/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "Goldfish swim quickly in clean water.", "Los peces dorados nadan rápido en agua limpia."),
        CambridgeVocabItem("frog", "rana", "/frɒɡ/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The green frog jumps onto a lily pad.", "La rana verde salta sobre una hoja de lirio."),
        CambridgeVocabItem("giraffe", "jirafa", "/dʒəˈrɑːf/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "A giraffe eats leaves from tall trees.", "Una jirafa come hojas de árboles altos."),
        CambridgeVocabItem("goat", "cabra", "/ɡəʊt/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The goat is climbing up the hill.", "La cabra está subiendo la colina."),
        CambridgeVocabItem("hippo", "hipopótamo", "/ˈhɪp.əʊ/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The hippo cools down in the muddy water.", "El hipopótamo se refresca en el agua lodosa."),
        CambridgeVocabItem("horse", "caballo", "/hɔːs/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "Can you ride a horse?", "¿Puedes montar a caballo?"),
        CambridgeVocabItem("jellyfish", "medusa", "/ˈdʒel.i.fɪʃ/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "Watch out for the pink jellyfish in the sea.", "Cuidado con la medusa rosa en el mar."),
        CambridgeVocabItem("lizard", "lagartija / lagarto", "/ˈlɪz.əd/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "A green lizard is sitting on the warm rock.", "Una lagartija verde está sentada en la roca caliente."),
        CambridgeVocabItem("monkey", "mono", "/ˈmʌŋ.ki/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The cheeky monkey is eating a yellow banana.", "El mono travieso está comiendo un plátano amarillo."),
        CambridgeVocabItem("mouse", "ratón", "/maʊs/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "A tiny mouse ran under the sofa.", "Un diminuto ratón corrió debajo del sofá."),
        CambridgeVocabItem("pet", "mascota", "/pet/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "Do you have a pet at home?", "¿Tienes una mascota en casa?"),
        CambridgeVocabItem("polar bear", "oso polar", "/ˌpəʊ.lə ˈbeər/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The white polar bear lives in the snow and ice.", "El oso polar blanco vive en la nieve y el hielo."),
        CambridgeVocabItem("sheep", "oveja", "/ʃiːp/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The fluffy sheep are resting in the green field.", "Las ovejas esponjosas descansan en el campo verde."),
        CambridgeVocabItem("snake", "serpiente / culebra", "/sneɪk/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The long snake is slithering through the grass.", "La larga serpiente se desliza entre la hierba."),
        CambridgeVocabItem("spider", "araña", "/ˈspaɪ.dər/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "There is a small spider making a web in the corner.", "Hay una pequeña araña haciendo una telaraña en la esquina."),
        CambridgeVocabItem("tail", "cola / rabo", "/teɪl/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The puppy wagged its happy tail.", "El cachorro movió su cola alegre."),
        CambridgeVocabItem("tiger", "tigre", "/ˈtaɪ.ɡər/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "The fierce tiger has orange and black stripes.", "El feroz tigre tiene rayas naranjas y negras."),
        CambridgeVocabItem("zebra", "cebra", "/ˈzeb.rə/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "A zebra looks like a striped horse.", "Una cebra parece un caballo rayado."),
        CambridgeVocabItem("zoo", "zoológico", "/zuː/", CambridgeYleLevel.STARTERS, "noun", "Animals (Animales)", "We saw monkeys and elephants at the city zoo.", "Vimos monos y elefantes en el zoológico de la ciudad."),

        // Movers (A1)
        CambridgeVocabItem("bat", "murciélago", "/bæt/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "Bats fly out from the cave at night.", "Los murciélagos salen volando de la cueva por la noche."),
        CambridgeVocabItem("cage", "jaula", "/keɪdʒ/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "The pet parrot is inside its large cage.", "El loro mascota está dentro de su gran jaula."),
        CambridgeVocabItem("dolphin", "delfín", "/ˈdɒl.fɪn/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "Dolphins are very clever sea mammals.", "Los delfines son mamíferos marinos muy inteligentes."),
        CambridgeVocabItem("kangaroo", "canguro", "/ˌkæŋ.ɡərˈuː/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "A kangaroo carries its baby in its pouch.", "Un canguro lleva a su cría en su bolsa."),
        CambridgeVocabItem("kitten", "gatito", "/ˈkɪt.ən/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "The soft kitten is playing with a ball of wool.", "El suave gatito está jugando con un ovillo de lana."),
        CambridgeVocabItem("lion", "león", "/ˈlaɪ.ən/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "The lion is known as the king of the jungle.", "El león es conocido como el rey de la selva."),
        CambridgeVocabItem("panda", "panda", "/ˈpæn.də/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "The giant panda loves eating green bamboo.", "El panda gigante adora comer bambú verde."),
        CambridgeVocabItem("parrot", "loro / papagayo", "/ˈpær.ət/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "The colourful parrot can repeat words.", "El loro colorido puede repetir palabras."),
        CambridgeVocabItem("penguin", "pingüino", "/ˈpeŋ.ɡwɪn/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "Penguins cannot fly, but they swim very fast.", "Los pingüinos no pueden volar, pero nadan muy rápido."),
        CambridgeVocabItem("puppy", "cachorro", "/ˈpʌp.i/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "Our puppy likes chasing balls in the garden.", "A nuestro cachorro le gusta perseguir pelotas en el jardín."),
        CambridgeVocabItem("rabbit", "conejo", "/ˈræb.ɪt/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "The white rabbit has long soft ears.", "El conejo blanco tiene orejas largas y suaves."),
        CambridgeVocabItem("shark", "tiburón", "/ʃɑːk/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "The great white shark swims deep in the ocean.", "El gran tiburón blanco nada en lo profundo del océano."),
        CambridgeVocabItem("snail", "caracol", "/sneɪl/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "A snail moves very slowly and carries its house.", "Un caracol se mueve muy despacio y carga su casa."),
        CambridgeVocabItem("whale", "ballena", "/weɪl/", CambridgeYleLevel.MOVERS, "noun", "Animals (Animales)", "The blue whale is the largest animal in the world.", "La ballena azul es el animal más grande del mundo."),

        // Flyers (A2)
        CambridgeVocabItem("beetle", "escarabajo", "/ˈbiː.təl/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "We saw a shiny green beetle in the woods.", "Vimos un brillante escarabajo verde en el bosque."),
        CambridgeVocabItem("butterfly", "mariposa", "/ˈbʌt.ə.flaɪ/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "A beautiful butterfly landed on her hand.", "Una hermosa mariposa se posó en su mano."),
        CambridgeVocabItem("camel", "camello", "/ˈkæm.əl/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "Camels can survive without water for many days in the desert.", "Los camellos pueden sobrevivir sin agua durante muchos días en el desierto."),
        CambridgeVocabItem("creature", "criatura / ser vivo", "/ˈkriː.tʃər/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "Mysterious sea creatures live in deep water.", "Misteriosas criaturas marinas viven en aguas profundas."),
        CambridgeVocabItem("dinosaur", "dinosaurio", "/ˈdaɪ.nə.sɔːr/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "Dinosaurs lived on Earth millions of years ago.", "Los dinosaurios vivieron en la Tierra hace millones de años."),
        CambridgeVocabItem("eagle", "águila", "/ˈiː.ɡəl/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "The eagle flew high above the mountain peaks.", "El águila voló alto por encima de los picos de las montañas."),
        CambridgeVocabItem("extinct", "extinto", "/ɪkˈstɪŋkt/", CambridgeYleLevel.FLYERS, "adj", "Animals (Animales)", "The dodo is an extinct bird.", "El dodo es un ave extinta."),
        CambridgeVocabItem("fur", "pelaje / piel", "/fɜːr/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "The cat has thick, warm fur in winter.", "El gato tiene un pelaje grueso y cálido en invierno."),
        CambridgeVocabItem("insect", "insecto", "/ˈɪn.sekt/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "Ants and bees are examples of insects.", "Las hormigas y las abejas son ejemplos de insectos."),
        CambridgeVocabItem("nest", "nido", "/nest/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "The robin built a small nest in the garden bush.", "El petirrojo construyó un pequeño nido en el arbusto del jardín."),
        CambridgeVocabItem("octopus", "pulpo", "/ˈɒk.tə.pəs/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "An octopus has eight arms and three hearts.", "Un pulpo tiene ocho brazos y tres corazones."),
        CambridgeVocabItem("swan", "cisne", "/swɒn/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "A white swan is gliding across the calm lake.", "Un cisne blanco se desliza por el lago tranquilo."),
        CambridgeVocabItem("tortoise", "tortuga terrestre", "/ˈtɔː.təs/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "The old tortoise lives in the botanic garden.", "La vieja tortuga terrestre vive en el jardín botánico."),
        CambridgeVocabItem("wild", "salvaje", "/waɪld/", CambridgeYleLevel.FLYERS, "adj", "Animals (Animales)", "Wild horses roam freely in the valley.", "Los caballos salvajes deambulan libremente por el valle."),
        CambridgeVocabItem("wing", "ala", "/wɪŋ/", CambridgeYleLevel.FLYERS, "noun", "Animals (Animales)", "The bird spread its strong wings and took off.", "El ave extendió sus fuertes alas y despegó."),

        // ================= 2. THE BODY AND THE FACE (CUERPO Y CARA) =================
        CambridgeVocabItem("arm", "brazo", "/ɑːm/", CambridgeYleLevel.STARTERS, "noun", "The body and the face (Cuerpo y Cara)", "Raise your right arm high.", "Levanta tu brazo derecho en alto."),
        CambridgeVocabItem("body", "cuerpo", "/ˈbɒd.i/", CambridgeYleLevel.STARTERS, "noun", "The body and the face (Cuerpo y Cara)", "Exercise keeps your whole body healthy.", "El ejercicio mantiene sano todo tu cuerpo."),
        CambridgeVocabItem("ear", "oreja / oído", "/ɪər/", CambridgeYleLevel.STARTERS, "noun", "The body and the face (Cuerpo y Cara)", "We hear sounds with our ears.", "Escuchamos sonidos con nuestros oídos."),
        CambridgeVocabItem("eye", "ojo", "/aɪ/", CambridgeYleLevel.STARTERS, "noun", "The body and the face (Cuerpo y Cara)", "She has bright green eyes.", "Ella tiene ojos verdes brillantes."),
        CambridgeVocabItem("face", "cara / rostro", "/feɪs/", CambridgeYleLevel.STARTERS, "noun", "The body and the face (Cuerpo y Cara)", "Wash your face with warm water.", "Lávate la cara con agua tibia."),
        CambridgeVocabItem("foot", "pie (plural: feet)", "/fʊt/", CambridgeYleLevel.STARTERS, "noun", "The body and the face (Cuerpo y Cara)", "Put your shoes on your feet.", "Ponte los zapatos en los pies."),
        CambridgeVocabItem("hair", "cabello / pelo", "/heər/", CambridgeYleLevel.STARTERS, "noun", "The body and the face (Cuerpo y Cara)", "He has short curly brown hair.", "Él tiene cabello castaño, corto y rizado."),
        CambridgeVocabItem("hand", "mano", "/hænd/", CambridgeYleLevel.STARTERS, "noun", "The body and the face (Cuerpo y Cara)", "Clap your hands to the music.", "Aplaude con tus manos al ritmo de la música."),
        CambridgeVocabItem("head", "cabeza", "/hed/", CambridgeYleLevel.STARTERS, "noun", "The body and the face (Cuerpo y Cara)", "Wear a helmet to protect your head.", "Usa un casco para proteger tu cabeza."),
        CambridgeVocabItem("leg", "pierna", "/leɡ/", CambridgeYleLevel.STARTERS, "noun", "The body and the face (Cuerpo y Cara)", "He hurt his left leg while playing football.", "Se lastimó la pierna izquierda jugando al fútbol."),
        CambridgeVocabItem("mouth", "boca", "/maʊθ/", CambridgeYleLevel.STARTERS, "noun", "The body and the face (Cuerpo y Cara)", "Open your mouth and say 'ah'.", "Abre la boca y di 'ah'."),
        CambridgeVocabItem("nose", "nariz", "/nəʊz/", CambridgeYleLevel.STARTERS, "noun", "The body and the face (Cuerpo y Cara)", "We smell flowers with our nose.", "Olemos flores con nuestra nariz."),
        CambridgeVocabItem("smile", "sonreír / sonrisa", "/smaɪl/", CambridgeYleLevel.STARTERS, "verb", "The body and the face (Cuerpo y Cara)", "She greeted everyone with a lovely smile.", "Saludó a todos con una hermosa sonrisa."),

        CambridgeVocabItem("back", "espalda", "/bæk/", CambridgeYleLevel.MOVERS, "noun", "The body and the face (Cuerpo y Cara)", "Sit straight to keep your back healthy.", "Siéntate derecho para cuidar tu espalda."),
        CambridgeVocabItem("beard", "barba", "/bɪəd/", CambridgeYleLevel.MOVERS, "noun", "The body and the face (Cuerpo y Cara)", "My grandfather has a neat grey beard.", "Mi abuelo tiene una barba gris prolija."),
        CambridgeVocabItem("blond", "rubio / rubia", "/blɒnd/", CambridgeYleLevel.MOVERS, "adj", "The body and the face (Cuerpo y Cara)", "She has long wavy blond hair.", "Ella tiene cabello rubio, largo y ondulado."),
        CambridgeVocabItem("curly", "rizado / ondulado", "/ˈkɜː.li/", CambridgeYleLevel.MOVERS, "adj", "The body and the face (Cuerpo y Cara)", "His baby sister has sweet curly hair.", "Su hermanita bebé tiene un lindo cabello rizado."),
        CambridgeVocabItem("moustache", "bigote", "/məˈstɑːʃ/", CambridgeYleLevel.MOVERS, "noun", "The body and the face (Cuerpo y Cara)", "The detective in the movie had a black moustache.", "El detective de la película tenía un bigote negro."),
        CambridgeVocabItem("neck", "cuello", "/nek/", CambridgeYleLevel.MOVERS, "noun", "The body and the face (Cuerpo y Cara)", "He wore a warm scarf around his neck.", "Llevaba una bufanda abrigada alrededor de su cuello."),
        CambridgeVocabItem("shoulder", "hombro", "/ˈʃəʊl.dər/", CambridgeYleLevel.MOVERS, "noun", "The body and the face (Cuerpo y Cara)", "She carried her school backpack on one shoulder.", "Llevaba su mochila escolar sobre un hombro."),
        CambridgeVocabItem("stomach", "estómago / barriga", "/ˈstʌm.ək/", CambridgeYleLevel.MOVERS, "noun", "The body and the face (Cuerpo y Cara)", "My stomach is rumbling because I am hungry.", "Me ruge el estómago porque tengo hambre."),
        CambridgeVocabItem("tooth", "diente (plural: teeth)", "/tuːθ/", CambridgeYleLevel.MOVERS, "noun", "The body and the face (Cuerpo y Cara)", "Brush your teeth twice every day.", "Cepíllate los dientes dos veces al día."),

        CambridgeVocabItem("elbow", "codo", "/ˈel.bəʊ/", CambridgeYleLevel.FLYERS, "noun", "The body and the face (Cuerpo y Cara)", "Resting your elbows on the table is informal.", "Apoyar los codos en la mesa es informal."),
        CambridgeVocabItem("finger", "dedo de la mano", "/ˈfɪŋ.ɡər/", CambridgeYleLevel.FLYERS, "noun", "The body and the face (Cuerpo y Cara)", "He pointed with his index finger.", "Señaló con su dedo índice."),
        CambridgeVocabItem("knee", "rodilla", "/niː/", CambridgeYleLevel.FLYERS, "noun", "The body and the face (Cuerpo y Cara)", "She scraped her knee when she fell off the scooter.", "Se raspó la rodilla cuando se cayó del monopatín."),
        CambridgeVocabItem("toe", "dedo del pie", "/təʊ/", CambridgeYleLevel.FLYERS, "noun", "The body and the face (Cuerpo y Cara)", "Humans have ten toes on their feet.", "Los seres humanos tienen diez dedos en los pies."),

        // ================= 3. CLOTHES (ROPA) =================
        CambridgeVocabItem("baseball cap", "gorra de béisbol", "/ˈbeɪs.bɔːl kæp/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "He wore a blue baseball cap in the sun.", "Llevaba una gorra de béisbol azul bajo el sol."),
        CambridgeVocabItem("boots", "botas", "/buːts/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "Put on your rain boots before walking outside.", "Ponte las botas de lluvia antes de salir a caminar."),
        CambridgeVocabItem("clothes", "ropa", "/kləʊðz/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "Fold your clean clothes neatly.", "Dobla tu ropa limpia con prolijidad."),
        CambridgeVocabItem("dress", "vestido", "/dres/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "She chose a red dress for her birthday party.", "Eligió un vestido rojo para su fiesta de cumpleaños."),
        CambridgeVocabItem("glasses", "gafas / lentes", "/ˈɡlɑː.sɪz/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "Grandmother needs reading glasses.", "La abuela necesita gafas para leer."),
        CambridgeVocabItem("handbag", "bolso / cartera", "/ˈhænd.bæɡ/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "She keeps her keys in her handbag.", "Ella guarda sus llaves en su bolso de mano."),
        CambridgeVocabItem("hat", "sombrero", "/hæt/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "He tipped his straw hat politely.", "Se inclinó el sombrero de paja con cortesía."),
        CambridgeVocabItem("jacket", "chaqueta / chamarra", "/ˈdʒæk.ɪt/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "Zip up your jacket; it is chilly outside.", "Súbete la cremallera de la chaqueta; hace frío afuera."),
        CambridgeVocabItem("jeans", "pantalones vaqueros / jeans", "/dʒiːnz/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "Blue jeans are comfortable for everyday wear.", "Los jeans azules son cómodos para el uso diario."),
        CambridgeVocabItem("shirt", "camisa", "/ʃɜːt/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "He wore a white buttoned shirt to school.", "Llevaba una camisa blanca con botones a la escuela."),
        CambridgeVocabItem("shoe", "zapato", "/ʃuː/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "Tie the laces on your running shoes.", "Átate los cordones de tus zapatillas deportivas."),
        CambridgeVocabItem("shorts", "pantalones cortos / shorts", "/ʃɔːts/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "In summer we wear shorts and sandals.", "En verano usamos pantalones cortos y sandalias."),
        CambridgeVocabItem("skirt", "falda", "/skɜːt/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "The school uniform includes a dark blue skirt.", "El uniforme escolar incluye una falda azul oscura."),
        CambridgeVocabItem("sock", "calcetín", "/sɒk/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "Where is my matching pair of socks?", "¿Dónde está mi par de calcetines a juego?"),
        CambridgeVocabItem("trousers", "pantalones", "/ˈtraʊ.zəz/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "His grey trousers are clean and ironed.", "Sus pantalones grises están limpios y planchados."),
        CambridgeVocabItem("T-shirt", "playera / camiseta", "/ˈtiː.ʃɜːt/", CambridgeYleLevel.STARTERS, "noun", "Clothes (Ropa y Accesorios)", "I bought a cotton T-shirt at the shop.", "Compré una camiseta de algodón en la tienda."),

        CambridgeVocabItem("coat", "abrigo", "/kəʊt/", CambridgeYleLevel.MOVERS, "noun", "Clothes (Ropa y Accesorios)", "Take your winter coat when it snows.", "Lleva tu abrigo de invierno cuando nieve."),
        CambridgeVocabItem("helmet", "casco de protección", "/ˈhel.mət/", CambridgeYleLevel.MOVERS, "noun", "Clothes (Ropa y Accesorios)", "Always fasten your helmet when cycling.", "Abróchate siempre el casco al andar en bicicleta."),
        CambridgeVocabItem("scarf", "bufanda", "/skɑːf/", CambridgeYleLevel.MOVERS, "noun", "Clothes (Ropa y Accesorios)", "Wrap a woollen scarf around your neck.", "Envuélvete una bufanda de lana alrededor del cuello."),
        CambridgeVocabItem("sweater", "suéter / jersey", "/ˈswet.ər/", CambridgeYleLevel.MOVERS, "noun", "Clothes (Ropa y Accesorios)", "This green knitted sweater is warm.", "Este suéter tejido verde es muy abrigado."),
        CambridgeVocabItem("swimsuit", "traje de baño / bañador", "/ˈswɪm.suːt/", CambridgeYleLevel.MOVERS, "noun", "Clothes (Ropa y Accesorios)", "Pack your swimsuit for the hotel pool.", "Empaca tu traje de baño para la piscina del hotel."),

        CambridgeVocabItem("belt", "cinturón", "/belt/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "Fasten your leather belt securely.", "Asegura tu cinturón de cuero."),
        CambridgeVocabItem("bracelet", "pulsera / brazalete", "/ˈbreɪ.slət/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "She wears a silver charm bracelet.", "Ella lleva una pulsera de dijes de plata."),
        CambridgeVocabItem("costume", "disfraz / traje", "/ˈkɒs.tʃuːm/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "He dressed up in a pirate costume.", "Se vistió con un disfraz de pirata."),
        CambridgeVocabItem("crown", "corona", "/kraʊn/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "The queen wore a sparkling gold crown.", "La reina llevaba una corona de oro resplandeciente."),
        CambridgeVocabItem("glove", "guante", "/ɡlʌv/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "Wear thick gloves to make snowballs.", "Ponte guantes gruesos para hacer bolas de nieve."),
        CambridgeVocabItem("necklace", "collar", "/ˈnek.ləs/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "She received a pearl necklace as a gift.", "Recibió un collar de perlas como regalo."),
        CambridgeVocabItem("pyjamas", "pijama", "/pɪˈdʒɑː.məz/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "Put on your soft pyjamas before going to bed.", "Ponte tu pijama suave antes de acostarte."),
        CambridgeVocabItem("pocket", "bolsillo", "/ˈpɒk.ɪt/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "He placed his compass in his coat pocket.", "Guardó su brújula en el bolsillo del abrigo."),
        CambridgeVocabItem("ring", "anillo", "/rɪŋ/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "The gold ring has a tiny blue stone.", "El anillo de oro tiene una pequeña piedra azul."),
        CambridgeVocabItem("sunglasses", "gafas de sol", "/ˈsʌŋˌɡlɑː.sɪz/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "Wear sunglasses to protect your eyes on sunny days.", "Usa gafas de sol para proteger tus ojos en días soleados."),
        CambridgeVocabItem("trainers", "zapatillas deportivas", "/ˈtreɪ.nəz/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "He laced up his new running trainers.", "Se ató las nuevas zapatillas deportivas."),
        CambridgeVocabItem("umbrella", "paraguas", "/ʌmˈbrel.ə/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "Take an umbrella because it might rain.", "Lleva un paraguas porque podría llover."),
        CambridgeVocabItem("uniform", "uniforme", "/ˈjuː.nɪ.fɔːm/", CambridgeYleLevel.FLYERS, "noun", "Clothes (Ropa y Accesorios)", "The pilot wore a smart dark blue uniform.", "El piloto vestía un elegante uniforme azul oscuro."),

        // ================= 4. FOOD & DRINK (COMIDA Y BEBIDA) =================
        CambridgeVocabItem("apple", "manzana", "/ˈæp.əl/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "An apple a day keeps the doctor away.", "Una manzana al día mantiene lejos al médico."),
        CambridgeVocabItem("banana", "plátano / banana", "/bəˈnɑː.nə/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "Peel the yellow banana before eating.", "Pela el plátano amarillo antes de comerlo."),
        CambridgeVocabItem("bread", "pan", "/bred/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "Fresh bread smells delicious in the morning.", "El pan fresco huele delicioso por la mañana."),
        CambridgeVocabItem("breakfast", "desayuno", "/ˈbrek.fəst/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "Breakfast gives you energy for the morning.", "El desayuno te da energía para la mañana."),
        CambridgeVocabItem("burger", "hamburguesa", "/ˈbɜː.ɡər/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "We ordered a burger with salad and cheese.", "Pedimos una hamburguesa con ensalada y queso."),
        CambridgeVocabItem("cake", "pastel / tarta", "/keɪk/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "There are eight candles on the birthday cake.", "Hay ocho velas en el pastel de cumpleaños."),
        CambridgeVocabItem("carrot", "zanahoria", "/ˈkær.ət/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "Rabbits love to crunch on orange carrots.", "A los conejos les encanta masticar zanahorias naranjas."),
        CambridgeVocabItem("chocolate", "chocolate", "/ˈtʃɒk.lət/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "Hot chocolate warms you up in winter.", "El chocolate caliente te reconforta en invierno."),
        CambridgeVocabItem("egg", "huevo", "/eɡ/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "He boiled two eggs for breakfast.", "Hirvió dos huevos para el desayuno."),
        CambridgeVocabItem("ice cream", "helado", "/ˌaɪs ˈkriːm/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "I chose strawberry ice cream on a cone.", "Elegí helado de fresa en un cono."),
        CambridgeVocabItem("juice", "jugo / zumo", "/dʒuːs/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "Would you like a glass of orange juice?", "¿Te gustaría un vaso de jugo de naranja?"),
        CambridgeVocabItem("lemonade", "limonada", "/ˌlem.əˈneɪd/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "Cold lemonade with mint is refreshing.", "La limonada fría con menta es refrescante."),
        CambridgeVocabItem("milk", "leche", "/mɪlk/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "Cats and kittens enjoy fresh milk.", "A los gatos y gatitos les gusta la leche fresca."),
        CambridgeVocabItem("rice", "arroz", "/raɪs/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "Steamed rice is served with vegetables.", "El arroz al vapor se sirve con verduras."),
        CambridgeVocabItem("tomato", "tomate", "/təˈmɑː.təʊ/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "Add ripe red tomatoes to the salad.", "Agrega tomates rojos maduros a la ensalada."),
        CambridgeVocabItem("watermelon", "sandía", "/ˈwɔː.təˌmel.ən/", CambridgeYleLevel.STARTERS, "noun", "Food & drink (Comida y Bebida)", "Watermelon is sweet, cool and full of juice.", "La sandía es dulce, fresca y jugosa."),

        CambridgeVocabItem("cheese", "queso", "/tʃiːz/", CambridgeYleLevel.MOVERS, "noun", "Food & drink (Comida y Bebida)", "Melted cheese tastes wonderful on pizza.", "El queso derretido sabe maravilloso en la pizza."),
        CambridgeVocabItem("coffee", "café", "/ˈkɒf.i/", CambridgeYleLevel.MOVERS, "noun", "Food & drink (Comida y Bebida)", "Adults often drink coffee in the morning.", "Los adultos suelen beber café por la mañana."),
        CambridgeVocabItem("noodles", "fideos", "/ˈnuː.dəlz/", CambridgeYleLevel.MOVERS, "noun", "Food & drink (Comida y Bebida)", "We had warm vegetable noodles for lunch.", "Almorzamos fideos calientes de verduras."),
        CambridgeVocabItem("pancake", "panqueque / tortita", "/ˈpæn.keɪk/", CambridgeYleLevel.MOVERS, "noun", "Food & drink (Comida y Bebida)", "She poured honey over her fluffy pancakes.", "Vertió miel sobre sus panqueques esponjosos."),
        CambridgeVocabItem("pasta", "pasta", "/ˈpæs.tə/", CambridgeYleLevel.MOVERS, "noun", "Food & drink (Comida y Bebida)", "Italian pasta with tomato sauce is delicious.", "La pasta italiana con salsa de tomate es deliciosa."),
        CambridgeVocabItem("salad", "ensalada", "/ˈsæl.əd/", CambridgeYleLevel.MOVERS, "noun", "Food & drink (Comida y Bebida)", "A fresh green salad has lettuce, cucumber, and olive oil.", "Una ensalada verde fresca lleva lechuga, pepino y aceite de oliva."),
        CambridgeVocabItem("sandwich", "sándwich / emparedado", "/ˈsæn.wɪdʒ/", CambridgeYleLevel.MOVERS, "noun", "Food & drink (Comida y Bebida)", "Pack a cheese sandwich for the picnic.", "Empaca un sándwich de queso para el picnic."),
        CambridgeVocabItem("soup", "sopa", "/suːp/", CambridgeYleLevel.MOVERS, "noun", "Food & drink (Comida y Bebida)", "A warm bowl of chicken soup cures colds.", "Un tazón caliente de sopa de pollo alivia los resfriados."),
        CambridgeVocabItem("tea", "té", "/tiː/", CambridgeYleLevel.MOVERS, "noun", "Food & drink (Comida y Bebida)", "They sat by the window sipping hot tea.", "Se sentaron junto a la ventana bebiendo té caliente."),
        CambridgeVocabItem("vegetable", "verdura / vegetal", "/ˈvedʒ.tə.bəl/", CambridgeYleLevel.MOVERS, "noun", "Food & drink (Comida y Bebida)", "Eating vegetables every day makes you strong.", "Comer verduras todos los días te hace fuerte."),

        CambridgeVocabItem("biscuit", "galleta (biscuit / cookie)", "/ˈbɪs.kɪt/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "Dip the crunchy biscuit in your milk.", "Sumerge la galleta crujiente en tu leche."),
        CambridgeVocabItem("butter", "mantequilla", "/ˈbʌt.ər/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "Spread creamy butter over warm toast.", "Unta mantequilla cremosa sobre la tostada caliente."),
        CambridgeVocabItem("cereal", "cereal", "/ˈsɪə.ri.əl/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "He ate a bowl of crispy cereal with cold milk.", "Comió un tazón de cereal crujiente con leche fría."),
        CambridgeVocabItem("chopsticks", "palillos chinos", "/ˈtʃɒp.stɪks/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "Using chopsticks requires good finger control.", "Usar palillos chinos requiere buen control de los dedos."),
        CambridgeVocabItem("flour", "harina", "/flaʊər/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "We mix flour, water, and yeast to bake bread.", "Mezclamos harina, agua y levadura para hornear pan."),
        CambridgeVocabItem("fork", "tenedor", "/fɔːk/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "Hold your fork in your left hand.", "Sostén el tenedor en la mano izquierda."),
        CambridgeVocabItem("honey", "miel", "/ˈhʌn.i/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "Bees produce natural sweet golden honey.", "Las abejas producen miel dorada, dulce y natural."),
        CambridgeVocabItem("jam", "mermelada", "/dʒæm/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "Strawberry jam tastes wonderful with butter on bread.", "La mermelada de fresa sabe maravillosa con mantequilla sobre el pan."),
        CambridgeVocabItem("knife", "cuchillo", "/naɪf/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "Use a butter knife to slice the cheese.", "Usa un cuchillo de mantequilla para cortar el queso."),
        CambridgeVocabItem("olives", "aceitunas / olivas", "/ˈɒl.ɪvz/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "Green and black olives grow in sunny Mediterranean groves.", "Las aceitunas verdes y negras crecen en campos soleados del Mediterráneo."),
        CambridgeVocabItem("pizza", "pizza", "/ˈpiːt.sə/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "We shared a large cheese and tomato pizza.", "Compartimos una pizza grande de queso y tomate."),
        CambridgeVocabItem("salt", "sal", "/sɒlt/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "Add a pinch of salt to enhance the soup's flavour.", "Añade una pizca de sal para realzar el sabor de la sopa."),
        CambridgeVocabItem("strawberry", "fresa / frutilla", "/ˈstrɔː.bər.i/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "Fresh red strawberries are sweet in summer.", "Las fresas rojas frescas son dulces en verano."),
        CambridgeVocabItem("sugar", "azúcar", "/ˈʃʊɡ.ər/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "Too much sugar is bad for your teeth.", "Demasiado azúcar es perjudicial para tus dientes."),
        CambridgeVocabItem("yoghurt", "yogur", "/ˈjɒɡ.ət/", CambridgeYleLevel.FLYERS, "noun", "Food & drink (Comida y Bebida)", "Natural Greek yoghurt with honey is delicious.", "El yogur griego natural con miel es delicioso.")
    )
}
