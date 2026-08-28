package com.example.data.local.seed

import com.example.data.local.entities.TopicEntity

object TopicSeedDataA1A2 {

    fun getA1Topics(startOrder: Int = 1): List<TopicEntity> {
        val list = mutableListOf<TopicEntity>()
        var order = startOrder

        fun addTopic(
            id: String,
            title: String,
            titleSpanish: String,
            category: String,
            moduleGroup: String,
            explanation: String,
            examples: List<Pair<String, String>>,
            mistakes: List<Pair<String, String>>,
            glossary: List<Pair<String, String>>,
            estMin: Int = 15
        ) {
            val examplesJson = examples.joinToString(prefix = "[", postfix = "]") { (en, es) ->
                "{\"en\":\"${en.replace("\"", "\\\"")}\",\"es\":\"${es.replace("\"", "\\\"")}\"}"
            }
            val mistakesJson = mistakes.joinToString(prefix = "[", postfix = "]") { (err, fix) ->
                "{\"error\":\"${err.replace("\"", "\\\"")}\",\"fix\":\"${fix.replace("\"", "\\\"")}\"}"
            }
            val glossaryJson = glossary.joinToString(prefix = "[", postfix = "]") { (term, def) ->
                "{\"term\":\"${term.replace("\"", "\\\"")}\",\"def\":\"${def.replace("\"", "\\\"")}\"}"
            }

            list.add(
                TopicEntity(
                    id = id,
                    title = title,
                    titleSpanish = titleSpanish,
                    category = category,
                    moduleGroup = moduleGroup,
                    orderIndex = order++,
                    explanation = explanation,
                    examplesJson = examplesJson,
                    commonMistakesJson = mistakesJson,
                    miniGlossaryJson = glossaryJson,
                    difficulty = "A1",
                    estimatedMinutes = estMin,
                    status = "NOT_STARTED"
                )
            )
        }

        // ==========================================
        // A1: GRAMÁTICA
        // ==========================================
        addTopic(
            id = "a1_gram_01",
            title = "Verb To Be: Am, Is, Are (Affirmative, Negative & Questions)",
            titleSpanish = "Verbo To Be: Ser y Estar",
            category = "Gramática",
            moduleGroup = "Fundamentos A1",
            explanation = "El verbo 'to be' significa 'ser' o 'estar'. Se conjuga: I am (I'm), You are (You're), He/She/It is (He's/She's/It's), We/They are (We're/They're). Para negar, añade 'not' después del verbo (I am not, she isn't, they aren't). Para preguntar, invierte el orden: verbo + sujeto ('Are you a student?').",
            examples = listOf(
                "I am from Mexico and I am twenty years old." to "Soy de México y tengo veinte años.",
                "She is not tired; she is very happy." to "Ella no está cansada; está muy feliz.",
                "Are they at school right now?" to "¿Están ellos en la escuela en este momento?"
            ),
            mistakes = listOf(
                "I have 20 years old." to "I am 20 years old. (En inglés la edad se dice con el verbo 'to be', no con 'have')",
                "He are my friend." to "He is my friend. (Tercera persona singular usa 'is')"
            ),
            glossary = listOf(
                "Subject pronoun" to "Pronombres de sujeto: I, you, he, she, it, we, they.",
                "Contraction" to "Forma corta: I'm, isn't, aren't."
            )
        )

        addTopic(
            id = "a1_gram_02",
            title = "Subject Pronouns & Possessive Adjectives (My, Your, His, Her...)",
            titleSpanish = "Pronombres Personales y Adjetivos Posesivos",
            category = "Gramática",
            moduleGroup = "Fundamentos A1",
            explanation = "Los pronombres de sujeto realizan la acción (I, you, he, she, it, we, they). Los adjetivos posesivos van antes de un sustantivo para indicar pertenencia: my (mi), your (tu/su), his (su de él), her (su de ella), its (su de cosa/animal), our (nuestro), their (su de ellos).",
            examples = listOf(
                "This is Maria and that is her car." to "Esta es María y ese es su auto (de ella).",
                "Carlos is reading his new book." to "Carlos está leyendo su nuevo libro (de él).",
                "Our house has a small garden." to "Nuestra casa tiene un pequeño jardín."
            ),
            mistakes = listOf(
                "Maria loves his brother." to "Maria loves her brother. (Usa 'her' para mujeres y 'his' para hombres)",
                "This is the dog and it's house." to "This is the dog and its house. ('Its' sin apóstrofe es el posesivo; 'it's' significa 'it is')"
            ),
            glossary = listOf(
                "Possession" to "Propiedad o pertenencia de un objeto o relación.",
                "Adjective" to "Palabra que describe o delimita a un sustantivo."
            )
        )

        addTopic(
            id = "a1_gram_03",
            title = "Articles: A, An & The (Indefinite vs. Definite)",
            titleSpanish = "Artículos: A, An y The",
            category = "Gramática",
            moduleGroup = "Fundamentos A1",
            explanation = "'A' y 'An' se usan con sustantivos singulares contables cuando se mencionan por primera vez o de forma general. Usa 'a' antes de sonido de consonante ('a book', 'a university') y 'an' antes de sonido de vocal ('an apple', 'an hour'). 'The' se usa para cosas específicas conocidas por ambos hablantes.",
            examples = listOf(
                "I bought an apple and a sandwich." to "Compré una manzana y un sándwich.",
                "The sandwich was delicious." to "El sándwich estaba delicioso (específico, el que acabo de comprar).",
                "She is an architect." to "Ella es arquitecta (las profesiones llevan 'a/an')."
            ),
            mistakes = listOf(
                "She is doctor." to "She is a doctor. (En inglés las profesiones siempre llevan 'a' o 'an')",
                "An university" to "A university (Empieza con sonido consonántico /j/)"
            ),
            glossary = listOf(
                "Vowel sound" to "Sonido vocálico inicial (/æ/, /e/, /ɪ/, /ɒ/, /ʌ/).",
                "Consonant sound" to "Sonido consonántico inicial."
            )
        )

        addTopic(
            id = "a1_gram_04",
            title = "Plural Nouns & Demonstratives (This, That, These, Those)",
            titleSpanish = "Sustantivos Plurales y Demostrativos",
            category = "Gramática",
            moduleGroup = "Fundamentos A1",
            explanation = "Plurales regulares: añade -s (books), -es tras sh/ch/s/x (watches, boxes), o -ies tras consonante + y (cities). Plurales irregulares: man->men, woman->women, child->children, foot->feet. Demostrativos: 'this' (este, cerca singular), 'that' (ese/aquel, lejos singular), 'these' (estos, cerca plural), 'those' (esos/aquellos, lejos plural).",
            examples = listOf(
                "This is my phone and that is your laptop over there." to "Este es mi teléfono y esa es tu laptop allá.",
                "These shoes are comfortable, but those boots are heavy." to "Estos zapatos son cómodos, pero aquellas botas son pesadas.",
                "Three children are playing in the park." to "Tres niños están jugando en el parque."
            ),
            mistakes = listOf(
                "These childs" to "These children (El plural de child es children)",
                "This apples are sweet." to "These apples are sweet. (Usa 'these' con sustantivos plurales)"
            ),
            glossary = listOf(
                "Near vs. Far" to "Cercanía física (this/these) vs. Lejanía física (that/those).",
                "Irregular plural" to "Sustantivo que cambia de raíz en plural."
            )
        )

        addTopic(
            id = "a1_gram_05",
            title = "Present Simple: Habits, Routines & 3rd Person -s",
            titleSpanish = "Presente Simple: Hábitos, Rutinas y Tercera Persona",
            category = "Gramática",
            moduleGroup = "Estructuras A1",
            explanation = "El Present Simple describe rutinas y hechos reales. Regla de oro: en afirmaciones con He, She, It se añade '-s' o '-es' al verbo (He works, She watches, It rains). Negación: 'don't + verbo' (I, you, we, they) / 'doesn't + verbo base' (he, she, it). Preguntas: 'Do you work?' / 'Does she work?'.",
            examples = listOf(
                "I live in a big apartment, but my brother lives in a house." to "Vivo en un departamento grande, pero mi hermano vive en una casa.",
                "She doesn't drink coffee in the evening." to "Ella no toma café por la tarde.",
                "Do you speak English every day? Yes, I do." to "¿Hablas inglés todos los días? Sí, así es."
            ),
            mistakes = listOf(
                "He work in a bank." to "He works in a bank. (No olvides la -s en tercera persona)",
                "She doesn't likes tea." to "She doesn't like tea. (Con 'doesn't', el verbo principal vuelve a su forma base)"
            ),
            glossary = listOf(
                "Third person" to "Tercera persona singular: he, she, it.",
                "Auxiliary verb" to "Verbo auxiliar do/does para negar y preguntar."
            )
        )

        addTopic(
            id = "a1_gram_06",
            title = "There is & There are + Prepositions of Place (In, On, Under, Next to)",
            titleSpanish = "There is / There are y Preposiciones de Lugar",
            category = "Gramática",
            moduleGroup = "Estructuras A1",
            explanation = "'There is' (There's) significa 'hay' para un solo objeto singular ('There is a book on the table'). 'There are' significa 'hay' para varios objetos plurales ('There are three chairs'). Preposiciones de lugar clave: in (dentro de), on (sobre / encima con contacto), under (debajo de), next to (al lado de), behind (detrás de), between (entre dos).",
            examples = listOf(
                "There is a supermarket next to my house." to "Hay un supermercado al lado de mi casa.",
                "There are many students in the classroom." to "Hay muchos estudiantes en el salón de clases.",
                "The cat is sleeping under the bed." to "El gato está durmiendo debajo de la cama."
            ),
            mistakes = listOf(
                "There is five chairs in the room." to "There are five chairs in the room. (Plural requiere 'there are')",
                "Have a book on the table." to "There is a book on the table. (Nunca uses 'have' para indicar existencia; usa 'there is/are')"
            ),
            glossary = listOf(
                "Preposition" to "Palabra que indica posición espacial o temporal.",
                "Existence" to "Indicar la presencia de cosas o personas con 'there is/are'."
            )
        )

        addTopic(
            id = "a1_gram_07",
            title = "Modal Can & Can't (Ability & Polite Requests)",
            titleSpanish = "Modal Can y Can't: Habilidad y Peticiones",
            category = "Gramática",
            moduleGroup = "Estructuras A1",
            explanation = "'Can' expresa capacidad física o mental ('I can swim'). 'Can't' (cannot) expresa imposibilidad ('He can't drive'). Se usa la misma forma para todas las personas (I can, she can, they can) seguida siempre de la forma base del verbo sin 'to'. Para pedir algo amablemente: 'Can you help me, please?'.",
            examples = listOf(
                "Can you speak French? No, I can't, but I can speak English." to "¿Puedes hablar francés? No, no puedo, pero sé hablar inglés.",
                "She can play the piano very well." to "Ella puede tocar el piano muy bien.",
                "Can I open the window, please?" to "¿Puedo abrir la ventana, por favor?"
            ),
            mistakes = listOf(
                "He can to swim." to "He can swim. (Nunca pongas 'to' después de 'can')",
                "She cans dance." to "She can dance. ('Can' nunca lleva -s en tercera persona)"
            ),
            glossary = listOf(
                "Ability" to "Capacidad o destreza para hacer algo.",
                "Permission" to "Pedir o conceder autorización de forma sencilla."
            )
        )

        // ==========================================
        // A1: VOCABULARIO
        // ==========================================
        addTopic(
            id = "a1_voc_01",
            title = "Greetings, Introductions & Personal Info",
            titleSpanish = "Saludos, Presentaciones y Datos Personales",
            category = "Vocabulario",
            moduleGroup = "Vocabulario A1",
            explanation = "Saludos: Hello, Hi, Good morning, Good afternoon, Good evening, Good night, Goodbye, See you later. Preguntas básicas: What is your name? Where are you from? How old are you? What do you do? Nice to meet you. Pleased to meet you.",
            examples = listOf(
                "Hello, my name is David. Nice to meet you!" to "Hola, mi nombre es David. ¡Mucho gusto!",
                "Where are you from? I am from Colombia." to "¿De dónde eres? Soy de Colombia.",
                "What is your phone number and email address?" to "¿Cuál es tu número de teléfono y correo electrónico?"
            ),
            mistakes = listOf(
                "Good night! (al llegar a un lugar por la tarde/noche)" to "Good evening! ('Good night' solo se usa para despedirse al ir a dormir)"
            ),
            glossary = listOf(
                "Introduction" to "Presentarse formal o informalmente a otra persona.",
                "Farewell" to "Despedida."
            )
        )

        addTopic(
            id = "a1_voc_02",
            title = "Numbers (1-100), Telling the Time & Days/Months",
            titleSpanish = "Números, Decir la Hora, Días y Meses",
            category = "Vocabulario",
            moduleGroup = "Vocabulario A1",
            explanation = "Días de la semana: Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday. Meses: January, February, March, April, May, June, July, August, September, October, November, December. Horas: 'What time is it?' -> 'It's three o'clock' (3:00), 'It's half past four' (4:30), 'It's quarter past five' (5:15), 'It's quarter to six' (5:45).",
            examples = listOf(
                "My birthday is on March 15th." to "Mi cumpleaños es el 15 de marzo.",
                "The English class starts at half past ten on Wednesdays." to "La clase de inglés empieza a las diez y media los miércoles.",
                "This jacket costs seventy-five dollars." to "Esta chamarra cuesta setenta y cinco dólares."
            ),
            mistakes = listOf(
                "In Monday" to "On Monday (Los días de la semana siempre llevan la preposición 'on')",
                "In 4 o'clock" to "At 4 o'clock (Las horas exactas siempre llevan la preposición 'at')"
            ),
            glossary = listOf(
                "O'clock" to "En punto (solo para horas exactas).",
                "Quarter past / to" to "Y cuarto / Cuarto para."
            )
        )

        addTopic(
            id = "a1_voc_03",
            title = "Family Members, Relationships & Describing People",
            titleSpanish = "Familia, Relaciones y Descripción Básica",
            category = "Vocabulario",
            moduleGroup = "Vocabulario A1",
            explanation = "Familia: father / dad, mother / mom, parents, brother, sister, son, daughter, children, grandfather, grandmother, uncle, aunt, cousin, husband, wife. Rasgos: tall, short, young, old, happy, kind, friendly, blue eyes, dark hair.",
            examples = listOf(
                "I have an older brother and two younger sisters." to "Tengo un hermano mayor y dos hermanas menores.",
                "My father is a teacher and my mother is a nurse." to "Mi padre es profesor y mi madre es enfermera.",
                "Her grandfather is seventy-two years old." to "Su abuelo tiene setenta y dos años."
            ),
            mistakes = listOf(
                "I have two brothers (cuando tienes un hermano y una hermana)" to "I have a brother and a sister / two siblings. ('Brothers' solo se refiere a varones; 'siblings' a hermanos en general)"
            ),
            glossary = listOf(
                "Parents" to "Padres (papá y mamá).",
                "Siblings" to "Hermanos y hermanas en general."
            )
        )

        addTopic(
            id = "a1_voc_04",
            title = "Food, Drinks, Supermarket & Basic Meals",
            titleSpanish = "Comida, Bebidas, Supermercado y Comidas del Día",
            category = "Vocabulario",
            moduleGroup = "Vocabulario A1",
            explanation = "Comidas del día: breakfast (desayuno), lunch (almuerzo/comida), dinner (cena). Alimentos comunes: bread, rice, chicken, fish, eggs, cheese, milk, water, juice, coffee, tea, apple, banana, tomato, potato. Expresiones: 'I'm hungry' (tengo hambre), 'I'm thirsty' (tengo sed), 'I like / I don't like'.",
            examples = listOf(
                "For breakfast, I usually have eggs, toast and orange juice." to "Para el desayuno, suelo comer huevos, pan tostado y jugo de naranja.",
                "I am thirsty; can I have a glass of water, please?" to "Tengo sed; ¿me das un vaso de agua, por favor?",
                "We need to buy milk, bread and butter at the grocery store." to "Necesitamos comprar leche, pan y mantequilla en la tienda de abarrotes."
            ),
            mistakes = listOf(
                "I have hunger." to "I am hungry. (En inglés se dice con el verbo 'to be': I am hungry / I am thirsty)"
            ),
            glossary = listOf(
                "Meal" to "Comida (evento de comer: desayuno, almuerzo, cena).",
                "Beverage / Drink" to "Bebida."
            )
        )

        // ==========================================
        // A1: COMUNICACIÓN Y HABILIDADES
        // ==========================================
        addTopic(
            id = "a1_func_01",
            title = "Essential Classroom English & Asking for Help",
            titleSpanish = "Inglés en el Aula y Pedir Ayuda",
            category = "Funciones Comunicativas",
            moduleGroup = "Interacción A1",
            explanation = "Frases imprescindibles para aprender y desenvolverte: 'How do you say ... in English?' (¿Cómo se dice ... en inglés?), 'What does ... mean?' (¿Qué significa ...?), 'Could you repeat that, please?' (¿Podrías repetir eso, por favor?), 'I don't understand' (No entiendo), 'How do you spell your surname?' (¿Cómo se deletrea tu apellido?).",
            examples = listOf(
                "Excuse me, teacher, what does 'schedule' mean?" to "Disculpe, profesor, ¿qué significa 'schedule'?",
                "How do you spell your last name? G-A-R-C-I-A." to "¿Cómo deletreas tu apellido? G-A-R-C-I-A.",
                "Can you speak more slowly, please?" to "¿Puede hablar más despacio, por favor?"
            ),
            mistakes = listOf(
                "How do you spell you name?" to "How do you spell your name? (Usa el adjetivo posesivo 'your')"
            ),
            glossary = listOf(
                "Spell" to "Deletrear letra por letra.",
                "Meaning" to "Significado o definición de una palabra."
            )
        )

        addTopic(
            id = "a1_pron_01",
            title = "The English Alphabet & Vowel Sounds /iː/ vs /ɪ/",
            titleSpanish = "El Alfabeto en Inglés y Vocales Cortas vs Largas",
            category = "Pronunciación",
            moduleGroup = "Fonética A1",
            explanation = "El alfabeto inglés tiene 26 letras. Presta atención a las letras problemáticas para hispanohablantes: A /eɪ/, E /iː/, I /aɪ/, J /dʒeɪ/, G /dʒiː/, H /eɪtʃ/, Y /waɪ/. Diferencia fundamental: vocal larga /iː/ (sheep, see, meet) con labios estirados vs vocal corta /ɪ/ (ship, sit, bit) relajada.",
            examples = listOf(
                "Sheep /ʃiːp/ (oveja) vs. Ship /ʃɪp/ (barco)." to "Oveja vs. Barco (contraste vocal larga vs corta).",
                "Leave /liːv/ (irse) vs. Live /lɪv/ (vivir)." to "Irse vs. Vivir.",
                "Spell the word 'APPLE': A-P-P-L-E." to "Deletreo de manzana: /eɪ/ - /piː/ - /piː/ - /el/ - /iː/."
            ),
            mistakes = listOf(
                "Pronunciar 'live' (vivir) exactamente igual a 'leave' (irse)." to "Mantén 'live' corta /lɪv/ y 'leave' larga /liːv/."
            ),
            glossary = listOf(
                "Long vowel" to "Vocal de sonido prolongado y tenso.",
                "Short vowel" to "Vocal de sonido breve y relajado."
            )
        )

        return list
    }

    fun getA2Topics(startOrder: Int = 1): List<TopicEntity> {
        val list = mutableListOf<TopicEntity>()
        var order = startOrder

        fun addTopic(
            id: String,
            title: String,
            titleSpanish: String,
            category: String,
            moduleGroup: String,
            explanation: String,
            examples: List<Pair<String, String>>,
            mistakes: List<Pair<String, String>>,
            glossary: List<Pair<String, String>>,
            estMin: Int = 16
        ) {
            val examplesJson = examples.joinToString(prefix = "[", postfix = "]") { (en, es) ->
                "{\"en\":\"${en.replace("\"", "\\\"")}\",\"es\":\"${es.replace("\"", "\\\"")}\"}"
            }
            val mistakesJson = mistakes.joinToString(prefix = "[", postfix = "]") { (err, fix) ->
                "{\"error\":\"${err.replace("\"", "\\\"")}\",\"fix\":\"${fix.replace("\"", "\\\"")}\"}"
            }
            val glossaryJson = glossary.joinToString(prefix = "[", postfix = "]") { (term, def) ->
                "{\"term\":\"${term.replace("\"", "\\\"")}\",\"def\":\"${def.replace("\"", "\\\"")}\"}"
            }

            list.add(
                TopicEntity(
                    id = id,
                    title = title,
                    titleSpanish = titleSpanish,
                    category = category,
                    moduleGroup = moduleGroup,
                    orderIndex = order++,
                    explanation = explanation,
                    examplesJson = examplesJson,
                    commonMistakesJson = mistakesJson,
                    miniGlossaryJson = glossaryJson,
                    difficulty = "A2",
                    estimatedMinutes = estMin,
                    status = "NOT_STARTED"
                )
            )
        }

        // ==========================================
        // A2: GRAMÁTICA
        // ==========================================
        addTopic(
            id = "a2_gram_01",
            title = "Past Simple: Verb To Be (Was / Were)",
            titleSpanish = "Pasado del Verbo To Be: Was y Were",
            category = "Gramática",
            moduleGroup = "Pasado A2",
            explanation = "El pasado de 'am' e 'is' es 'was' (I was, he was, she was, it was). El pasado de 'are' es 'were' (you were, we were, they were). Negativo: wasn't / weren't. Preguntas: 'Were you at home yesterday?' -> 'Yes, I was / No, I wasn't'.",
            examples = listOf(
                "I was at home last night because I was tired." to "Estaba en casa anoche porque estaba cansado.",
                "They were very happy with the exam results." to "Ellos estaban muy felices con los resultados del examen.",
                "Where were you born? I was born in Madrid." to "¿Dónde naciste? Nací en Madrid."
            ),
            mistakes = listOf(
                "I were at the party." to "I was at the party. (Con 'I' se usa 'was')",
                "She was born in 2002." to "Correcto: la expresión de nacer siempre usa 'was/were born'."
            ),
            glossary = listOf(
                "Past state" to "Estado o ubicación en un tiempo pasado.",
                "Born" to "Nacido (usado con was/were)."
            )
        )

        addTopic(
            id = "a2_gram_02",
            title = "Past Simple: Regular (-ed) & Irregular Verbs with Did",
            titleSpanish = "Pasado Simple: Verbos Regulares, Irregulares y Auxiliar Did",
            category = "Gramática",
            moduleGroup = "Pasado A2",
            explanation = "Verbos regulares: añade -ed (worked, watched, played, arrived). Verbos irregulares comunes: go->went, see->saw, have->had, buy->bought, eat->ate, come->came. En oraciones negativas usa 'didn't + verbo base' ('I didn't go'). En preguntas usa 'Did + sujeto + verbo base?' ('Did you see him?').",
            examples = listOf(
                "Yesterday, I went to the shopping mall and bought a new shirt." to "Ayer fui al centro comercial y compré una camisa nueva.",
                "We didn't have time to finish the report." to "No tuvimos tiempo de terminar el reporte.",
                "Did you watch the football match on TV last night?" to "¿Viste el partido de fútbol en la tele anoche?"
            ),
            mistakes = listOf(
                "I didn't went to school." to "I didn't go to school. (Con 'didn't' el verbo vuelve a su forma base 'go')",
                "Did she saw the movie?" to "Did she see the movie? (El auxiliar 'did' ya indica pasado)"
            ),
            glossary = listOf(
                "Regular verb" to "Verbo que forma el pasado añadiendo -ed.",
                "Irregular verb" to "Verbo que cambia su ortografía en pasado sin seguir una regla fija."
            )
        )

        addTopic(
            id = "a2_gram_03",
            title = "Countable vs. Uncountable Nouns (Some, Any, Much, Many, A lot of)",
            titleSpanish = "Sustantivos Contables e Incontables",
            category = "Gramática",
            moduleGroup = "Cantidades A2",
            explanation = "Contables (tienen plural: apples, cars): usan 'many' en preguntas/negaciones y 'a few' (unos pocos). Incontables (no tienen plural: water, sugar, money, time, information, bread): usan 'much' y 'a little' (un poco). 'Some' se usa en afirmaciones y ofrecimientos amables. 'Any' se usa en preguntas y negaciones ('I don't have any money'). 'A lot of' sirve para ambos en afirmaciones.",
            examples = listOf(
                "There is some water in the fridge, but there isn't any milk." to "Hay algo de agua en el refrigerador, pero no hay nada de leche.",
                "How much money do you need for the trip?" to "¿Cuánto dinero necesitas para el viaje?",
                "How many books did you read during the summer?" to "¿Cuántos libros leíste durante el verano?"
            ),
            mistakes = listOf(
                "How many money do you have?" to "How much money do you have? ('Money' es incontable; usa 'how much')",
                "I bought two breads." to "I bought two loaves/pieces of bread. ('Bread' es incontable en inglés)"
            ),
            glossary = listOf(
                "Countable" to "Elemento individual que se puede contar numéricamente (one chair, two chairs).",
                "Uncountable" to "Sustancia, masa o concepto que se mide pero no se cuenta en plurales."
            )
        )

        addTopic(
            id = "a2_gram_04",
            title = "Comparatives & Superlatives: Short & Long Adjectives",
            titleSpanish = "Comparativos y Superlativos Básicos",
            category = "Gramática",
            moduleGroup = "Modificadores A2",
            explanation = "Adjetivos cortos (1 sílaba): comparativo añade '-er than' (taller than, bigger than, faster than); superlativo 'the -est' (the tallest, the biggest). Adjetivos largos (2+ sílabas): 'more ... than' (more expensive than, more beautiful than); superlativo 'the most ...' (the most expensive). Irregulares: good -> better -> the best; bad -> worse -> the worst.",
            examples = listOf(
                "A car is faster than a bicycle, but an airplane is the fastest." to "Un auto es más rápido que una bicicleta, pero un avión es el más rápido.",
                "This restaurant is better than the one near my house." to "Este restaurante es mejor que el que está cerca de mi casa.",
                "Learning Japanese is more difficult than learning English." to "Aprender japonés es más difícil que aprender inglés."
            ),
            mistakes = listOf(
                "More better" to "Better (Nunca combines 'more' con formas irregulares)",
                "The most big" to "The biggest (Los adjetivos cortos de 1 sílaba usan la terminación '-est')"
            ),
            glossary = listOf(
                "Comparative" to "Compara dos personas, objetos o lugares.",
                "Superlative" to "Destaca un elemento como el máximo dentro de un grupo."
            )
        )

        addTopic(
            id = "a2_gram_05",
            title = "Future with Be Going To (Plans, Intentions & Visual Predictions)",
            titleSpanish = "Futuro con Be Going To: Planes e Intenciones",
            category = "Gramática",
            moduleGroup = "Futuro A2",
            explanation = "Estructura: 'am/is/are + going to + verbo base'. Se utiliza para hablar de planes o intenciones personales ya decididos para el futuro ('I am going to visit my parents next weekend') y para predicciones con evidencia clara en el presente ('Look at that person! He is going to fall').",
            examples = listOf(
                "I am going to start a new English course next Monday." to "Voy a comenzar un nuevo curso de inglés el próximo lunes.",
                "What are you going to do this weekend?" to "¿Qué vas a hacer este fin de semana?",
                "Look at those black clouds; it is going to rain." to "Mira esas nubes negras; va a llover."
            ),
            mistakes = listOf(
                "I am going visit London." to "I am going to visit London. (No olvides la partícula 'to' antes del verbo)",
                "She is going to buys a car." to "She is going to buy a car. (El verbo tras 'to' va en su forma base pura)"
            ),
            glossary = listOf(
                "Intention" to "Propósito o meta planificada previamente.",
                "Evidence" to "Indicio visible que respalda una predicción inmediata."
            )
        )

        addTopic(
            id = "a2_gram_06",
            title = "Rules & Obligations: Have to / Don't have to",
            titleSpanish = "Reglas y Obligaciones: Have to / Don't have to",
            category = "Gramática",
            moduleGroup = "Modales A2",
            explanation = "'Have to / Has to' indica que algo es obligatorio por una norma o necesidad ('I have to wear a uniform at work', 'She has to wake up at 6 AM'). 'Don't have to / Doesn't have to' significa que algo NO es obligatorio; eres libre de hacerlo o no ('You don't have to pay now; it's free today').",
            examples = listOf(
                "Students have to pass the exam to get the certificate." to "Los estudiantes tienen que aprobar el examen para obtener el certificado.",
                "You don't have to wash the dishes; I will do it." to "No tienes que lavar los platos; yo lo haré (no es necesario).",
                "Does he have to work on Saturdays?" to "¿Él tiene que trabajar los sábados?"
            ),
            mistakes = listOf(
                "He doesn't has to go." to "He doesn't have to go. (Con 'doesn't' se usa siempre 'have to')",
                "You mustn't go (prohibido) vs You don't have to go (opcional)." to "Usa 'don't have to' para indicar ausencia de obligación."
            ),
            glossary = listOf(
                "External rule" to "Regla o exigencia externa impuesta por una institución.",
                "Optional" to "No obligatorio; opcional."
            )
        )

        // ==========================================
        // A2: VOCABULARIO
        // ==========================================
        addTopic(
            id = "a2_voc_01",
            title = "Jobs, Workplaces & Daily Tasks",
            titleSpanish = "Profesiones, Lugares de Trabajo y Tareas",
            category = "Vocabulario",
            moduleGroup = "Vocabulario A2",
            explanation = "Profesiones: engineer, nurse, police officer, waiter / waitress, shop assistant, chef, driver, receptionist, accountant. Lugares: office, hospital, factory, restaurant, store, school. Acciones: answer phone calls, send emails, help customers, serve food, fix computers.",
            examples = listOf(
                "She is an accountant and works in an international office." to "Ella es contadora y trabaja en una oficina internacional.",
                "The waiter was very polite and served our food quickly." to "El mesero fue muy educado y sirvió nuestra comida rápidamente.",
                "My brother is studying to become a civil engineer." to "Mi hermano está estudiando para convertirse en ingeniero civil."
            ),
            mistakes = listOf(
                "I work like an engineer." to "I work as an engineer. (Para indicar tu profesión usa 'work as a/an')"
            ),
            glossary = listOf(
                "Workplace" to "Lugar físico donde se desempeña un empleo.",
                "Customer" to "Cliente que compra un producto o servicio."
            )
        )

        addTopic(
            id = "a2_voc_02",
            title = "Travel, Hotel Bookings & Asking for Directions",
            titleSpanish = "Viajes, Reservas de Hotel y Direcciones",
            category = "Vocabulario",
            moduleGroup = "Vocabulario A2",
            explanation = "Vocabulario de viaje: single / double room, check in / check out, book in advance, ticket, luggage, station, airport. Direcciones: turn left, turn right, go straight ahead, at the corner, opposite, across the street, near, far from here.",
            examples = listOf(
                "I would like to book a double room with breakfast included." to "Me gustaría reservar una habitación doble con desayuno incluido.",
                "Excuse me, how do I get to the train station from here?" to "Disculpe, ¿cómo llego a la estación de tren desde aquí?",
                "Go straight ahead for two blocks, then turn left at the corner." to "Vaya derecho por dos cuadras, luego gire a la izquierda en la esquina."
            ),
            mistakes = listOf(
                "Go directly" to "Go straight ahead / Go straight on. (Expresión natural para ir en línea recta)"
            ),
            glossary = listOf(
                "Check-in" to "Registro de llegada en hotel o aeropuerto.",
                "Opposite" to "Enfrente de, al otro lado."
            )
        )

        addTopic(
            id = "a2_voc_03",
            title = "Free Time, Hobbies, Sports & Entertainment",
            titleSpanish = "Pasatiempos, Deportes y Entretenimiento",
            category = "Vocabulario",
            moduleGroup = "Vocabulario A2",
            explanation = "Colocaciones con deportes: PLAY con deportes de pelota y juegos (play football, play tennis, play chess); GO con actividades terminadas en -ing (go swimming, go running, go cycling); DO con artes marciales y ejercicios individuales (do yoga, do karate, do gymnastics). Pasatiempos: listen to music, read novels, watch movies, hang out with friends.",
            examples = listOf(
                "I usually go running in the morning and do yoga in the evening." to "Normalmente salgo a correr por la mañana y hago yoga por la tarde.",
                "Do you want to play tennis with us this Saturday?" to "¿Quieres jugar tenis con nosotros este sábado?",
                "On weekends, I enjoy hanging out with my friends." to "Los fines de semana disfruto salir y pasar el rato con mis amigos."
            ),
            mistakes = listOf(
                "I play swimming." to "I go swimming. (Actividades con -ing usan 'go')",
                "I play yoga." to "I do yoga. (Ejercicios individuales y disciplinas usan 'do')"
            ),
            glossary = listOf(
                "Hang out" to "Pasar el rato relajado con amigos.",
                "Sport collocation" to "Combinación correcta del verbo con el tipo de deporte."
            )
        )

        // ==========================================
        // A2: FUNCIONES Y COMUNICACIÓN
        // ==========================================
        addTopic(
            id = "a2_func_01",
            title = "Making Suggestions & Invitations (Why don't we...? / Let's...)",
            titleSpanish = "Hacer Sugerencias e Invitaciones",
            category = "Funciones Comunicativas",
            moduleGroup = "Interacción A2",
            explanation = "Fórmulas útiles de nivel A2: 'Let's + verbo base' ('Let's go to the cinema'), 'Why don't we + verbo base?' ('Why don't we have pizza for dinner?'), 'Shall we + verbo base?' ('Shall we leave now?'), 'Would you like to + verbo?' ('Would you like to join us?'). Respuestas: 'That sounds great!', 'I'd love to, but I'm busy', 'Good idea!'.",
            examples = listOf(
                "Why don't we go to the museum this afternoon? That sounds like a great idea!" to "¿Por qué no vamos al museo esta tarde? ¡Suena como una gran idea!",
                "Would you like to come to my birthday party on Friday?" to "¿Te gustaría venir a mi fiesta de cumpleaños el viernes?",
                "Let's take a break and get some coffee." to "Hagamos una pausa y tomemos un café."
            ),
            mistakes = listOf(
                "Let's to go" to "Let's go ('Let's' va seguido directamente del verbo base sin 'to')"
            ),
            glossary = listOf(
                "Suggestion" to "Propuesta amigable para realizar una actividad conjunta.",
                "Invitation" to "Invitación formal o informal a un evento."
            )
        )

        addTopic(
            id = "a2_pron_01",
            title = "Past Regular -ed Pronunciation (/t/, /d/, /ɪd/)",
            titleSpanish = "Pronunciación de la Terminación de Pasado -ed",
            category = "Pronunciación",
            moduleGroup = "Fonética A2",
            explanation = "La terminación '-ed' se pronuncia de 3 formas: 1) /ɪd/ (añade sílaba extra) ÚNICAMENTE tras sonidos /t/ o /d/ (wanted, decided, started). 2) /t/ tras consonantes sordas /p, k, f, s, ʃ, tʃ/ (watched, cooked, stopped, laughed). 3) /d/ tras sonidos sonoros y vocales (played, cleaned, lived, loved). ¡Nunca pronuncies la 'e' de forma literal salvo en /ɪd/!",
            examples = listOf(
                "Wanted /ˈwɒntɪd/ y Decided /dɪˈsaɪdɪd/ (añaden sílaba /ɪd/)." to "Sonido /ɪd/ tras /t/ y /d/.",
                "Watched /wɒtʃt/ y Cooked /kʊkt/ (sonido /t/ final sin sílaba extra)." to "Sonido /t/ tras consonantes sordas.",
                "Played /pleɪd/ y Cleaned /kliːnd/ (sonido /d/ suave)." to "Sonido /d/ tras vocales y consonantes sonoras."
            ),
            mistakes = listOf(
                "Pronunciar 'played' como 'play-ed' con dos sílabas." to "Pronuncia 'played' en una sola sílaba: /pleɪd/."
            ),
            glossary = listOf(
                "Voiced sound" to "Sonido que produce vibración en las cuerdas vocales (/b/, /g/, /v/, vocales).",
                "Voiceless sound" to "Sonido producido sin vibración de cuerdas vocales (/p/, /t/, /k/, /s/)."
            )
        )

        return list
    }
}
