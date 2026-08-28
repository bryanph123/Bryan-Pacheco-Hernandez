package com.example.data.local.seed

import com.example.data.local.entities.TopicEntity

object TopicSeedData {
    fun getInitialTopics(): List<TopicEntity> {
        val list = mutableListOf<TopicEntity>()
        var order = 1

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
            difficulty: String = "B2",
            estMin: Int = 20
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
                    difficulty = difficulty,
                    estimatedMinutes = estMin,
                    status = "NOT_STARTED"
                )
            )
        }

        // ==========================================
        // 1. GRAMÁTICA (12 Módulos / 18 Temas)
        // ==========================================

        // Mod 1: Tiempos Verbales Avanzados
        addTopic(
            id = "gram_01",
            title = "Present Perfect Simple vs. Present Perfect Continuous",
            titleSpanish = "Presente Perfecto Simple vs. Continuo",
            category = "Gramática",
            moduleGroup = "Módulo 1: Tiempos Verbales Avanzados",
            explanation = "El Present Perfect Simple (have/has + participio) enfatiza el RESULTADO completado o la cantidad ('He leído 3 libros'). El Present Perfect Continuous (have/has been + -ing) enfatiza la DURACIÓN ininterrumpida o la actividad en sí ('He estado leyendo toda la tarde'). Con verbos de estado (know, like, belong), siempre usamos la forma simple.",
            examples = listOf(
                "I have fixed the server router." to "He reparado el router del servidor. (resultado completado)",
                "I have been fixing computers since 8 AM." to "He estado reparando computadoras desde las 8 AM. (duración/actividad)",
                "She has known him for five years." to "Ella lo conoce desde hace 5 años. (verbo de estado, sin -ing)"
            ),
            mistakes = listOf(
                "I am living here for two years." to "I have been living here for two years. (No uses presente simple/continuo para acciones iniciadas en el pasado que continúan)",
                "I have been knowing him." to "I have known him. (Los stative verbs no admiten continuous)"
            ),
            glossary = listOf(
                "Stative verb" to "Verbo de estado que describe condiciones o sentimientos (no acciones dinámicas).",
                "Continuous aspect" to "Aspecto verbal que enfatiza el proceso o la duración en curso."
            )
        )

        addTopic(
            id = "gram_02",
            title = "Past Perfect Simple vs. Past Perfect Continuous",
            titleSpanish = "Pasado Perfecto Simple vs. Continuo",
            category = "Gramática",
            moduleGroup = "Módulo 1: Tiempos Verbales Avanzados",
            explanation = "El Past Perfect (had + participio) describe el 'pasado del pasado', una acción anterior a otro evento pasado. El Past Perfect Continuous (had been + -ing) muestra una acción continua que se prolongó hasta que ocurrió otro hecho en el pasado o causó un resultado visible.",
            examples = listOf(
                "When the students arrived, the teacher had already set up the projector." to "Cuando los alumnos llegaron, el profesor ya había instalado el proyector.",
                "His eyes were tired because he had been coding all night." to "Tenía los ojos cansados porque había estado programando toda la noche."
            ),
            mistakes = listOf(
                "When I arrived, they already left." to "When I arrived, they had already left. (El evento previo requiere past perfect)"
            ),
            glossary = listOf(
                "Past of the past" to "Acción que ocurrió antes de otra acción en tiempo pasado.",
                "Result in the past" to "Evidencia visible de una actividad previa en el pasado."
            )
        )

        addTopic(
            id = "gram_03",
            title = "Future Continuous & Future Perfect (Simple & Continuous)",
            titleSpanish = "Futuro Continuo y Futuro Perfecto",
            category = "Gramática",
            moduleGroup = "Módulo 1: Tiempos Verbales Avanzados",
            explanation = "Future Continuous (will be + -ing): Acción en progreso en un momento específico del futuro ('At 10 AM tomorrow, I will be teaching'). Future Perfect (will have + participio): Acción que estará terminada ANTES de un punto en el futuro ('By Friday, I will have finished the network installation'). Clave: la preposición 'By' suele acompañar al Future Perfect.",
            examples = listOf(
                "This time tomorrow, we will be flying to London." to "A esta hora mañana, estaremos volando a Londres.",
                "By next month, I will have completed my B2 English course." to "Para el próximo mes, habré completado mi curso de inglés B2.",
                "By 2027, he will have been teaching for ten years." to "Para 2027, él habrá estado enseñando durante diez años."
            ),
            mistakes = listOf(
                "By tomorrow I will finish the report." to "By tomorrow I will have finished the report. (Con 'by + tiempo futuro' usa Future Perfect)"
            ),
            glossary = listOf(
                "By (preposition)" to "Para, no más tarde de (marca límite temporal).",
                "Milestone" to "Hito o punto de referencia en el tiempo."
            )
        )

        addTopic(
            id = "gram_04",
            title = "Habitual Actions: Used to, Would, Be used to & Get used to",
            titleSpanish = "Hábitos: Used to, Would, Be used to y Get used to",
            category = "Gramática",
            moduleGroup = "Módulo 1: Tiempos Verbales Avanzados",
            explanation = "1) 'Used to + infinitivo': hábitos o estados pasados que ya no ocurren. 2) 'Would + infinitivo': solo hábitos/acciones repetidas en el pasado (NO estados como 'would live'). 3) 'Be used to + sustantivo/-ing': estar acostumbrado a algo. 4) 'Get used to + sustantivo/-ing': el proceso de acostumbrarse.",
            examples = listOf(
                "I used to live in a small town, but now I live in Mexico City." to "Solía vivir en un pueblo pequeño, pero ahora vivo en CDMX. (Estado pasado)",
                "Every summer, we would swim in the river." to "Cada verano, nadábamos en el río. (Acción repetida)",
                "I am used to waking up early for school." to "Estoy acostumbrado a levantarme temprano para la escuela.",
                "He will soon get used to working with Linux servers." to "Pronto se acostumbrará a trabajar con servidores Linux."
            ),
            mistakes = listOf(
                "I used to living in Monterrey." to "I used to live in Monterrey. ('used to' para pasado va con infinitivo simple)",
                "I am used to wake up early." to "I am used to waking up early. ('be used to' va con -ing)"
            ),
            glossary = listOf(
                "Accustomed to" to "Acostumbrado a.",
                "Adaptation process" to "Proceso de adaptación expresado por 'get used to'."
            )
        )

        // Mod 2: Modales
        addTopic(
            id = "gram_05",
            title = "Modal Verbs of Deduction & Probability (Present)",
            titleSpanish = "Modales de Deducción y Probabilidad en Presente",
            category = "Gramática",
            moduleGroup = "Módulo 2: Verbos Modales",
            explanation = "Para deducir la realidad en el presente: 1) 'Must' (estoy 99% seguro de que SÍ es): 'He must be at home'. 2) 'Can't / Couldn't' (estoy 99% seguro de que NO es): 'That can't be true'. 3) 'Might / May / Could' (posibilidad del 50%): 'The server might be down due to maintenance'. ¡Nunca uses 'mustn't' para deducción negativa!",
            examples = listOf(
                "The lights are on, so the technician must be inside." to "Las luces están encendidas, así que el técnico debe estar adentro.",
                "It can't be a hardware failure; the components are brand new." to "No puede ser una falla de hardware; los componentes son nuevos.",
                "She might know how to solve the network subnetting issue." to "Ella podría saber cómo resolver el problema de subredes de red."
            ),
            mistakes = listOf(
                "It mustn't be him, he is in Spain." to "It can't be him, he is in Spain. (Usa 'can't', jamás 'mustn't' para deducción negativa)"
            ),
            glossary = listOf(
                "Deduction" to "Conclusión lógica basada en evidencia.",
                "Speculation" to "Hipótesis sobre algo incierto."
            )
        )

        addTopic(
            id = "gram_06",
            title = "Past Modals of Speculation & Regret (Must have, Could have, Should have)",
            titleSpanish = "Modales de Pasado: Especulación y Arrepentimiento",
            category = "Gramática",
            moduleGroup = "Módulo 2: Verbos Modales",
            explanation = "Estructura: Modal + have + Participio pasado. 1) 'Must have done': Seguro que ocurrió ('The hard drive must have failed'). 2) 'Can't / Couldn't have done': Imposible que ocurriera. 3) 'May / Might / Could have done': Pudo haber ocurrido. 4) 'Should have done': Debería haberlo hecho (arrepentimiento o crítica). 5) 'Needn't have done': Lo hizo pero no era necesario.",
            examples = listOf(
                "You should have backed up your database before updating." to "Debiste haber respaldado tu base de datos antes de actualizar.",
                "They can't have finished the exam in only twenty minutes." to "No es posible que hayan terminado el examen en solo 20 minutos.",
                "I needn't have printed all these handouts; the school has tablets." to "No era necesario que imprimiera todos estos folletos; la escuela tiene tabletas."
            ),
            mistakes = listOf(
                "You should to have told me." to "You should have told me. (Sin 'to' después del modal)",
                "He must has been tired." to "He must have been tired. (Siempre 'have', nunca 'has')"
            ),
            glossary = listOf(
                "Regret" to "Arrepentimiento o lamento por una acción pasada.",
                "Critique" to "Señalamiento de lo que hubiera sido prudente hacer."
            )
        )

        // Mod 3: Condicionales
        addTopic(
            id = "gram_07",
            title = "Third Conditional & Mixed Conditionals",
            titleSpanish = "Tercer Condicional y Condicionales Mixtos",
            category = "Gramática",
            moduleGroup = "Módulo 3: Oraciones Condicionales",
            explanation = "Tercer condicional: Pasado irreal y su resultado en el pasado (If + Past Perfect, would have + participio). Condicional mixto: Conecta pasado irreal con resultado en el presente (If + had + part, would + base) O una condición presente permanente con un resultado pasado (If + past simple, would have + part).",
            examples = listOf(
                "If I had studied computer science, I would have worked at Google." to "Si hubiera estudiado ciencias computacionales, habría trabajado en Google. (3rd Conditional)",
                "If I hadn't lost my keys yesterday, I wouldn't be locked out now." to "Si no hubiera perdido mis llaves ayer, no me habría quedado afuera ahora. (Mixed: Pasado -> Presente)",
                "If he were more organized, he wouldn't have missed the project deadline." to "Si él fuera más organizado (presente), no habría perdido la fecha límite (pasado)."
            ),
            mistakes = listOf(
                "If I would have known, I would have helped." to "If I had known, I would have helped. (Nunca 'would have' en la cláusula del 'if')"
            ),
            glossary = listOf(
                "Hypothetical condition" to "Situación imaginaria o contraria a los hechos reales.",
                "Unreal past" to "Uso de formas de pasado para expresar irrealidad o distancia temporal."
            )
        )

        addTopic(
            id = "gram_08",
            title = "Wish & If Only (Present, Past & Annoyance)",
            titleSpanish = "Wish e If Only: Deseos de Presente, Pasado y Molestia",
            category = "Gramática",
            moduleGroup = "Módulo 3: Oraciones Condicionales",
            explanation = "1) Deseo de cambiar el PRESENTE: 'I wish / If only + Past Simple' ('I wish I spoke fluent English'). 2) Arrepentimiento del PASADO: 'I wish / If only + Past Perfect' ('I wish I had installed an antivirus'). 3) Molestia o deseo de que OTRA persona/cosa cambie: 'I wish + would + infinitivo' ('I wish the students would stop talking'). ¡No uses 'would' para ti mismo con 'I wish I would'!",
            examples = listOf(
                "I wish I had more free time during the week." to "Ojalá tuviera más tiempo libre entre semana.",
                "If only we had upgraded the server RAM earlier!" to "¡Si tan solo hubiéramos ampliado la memoria RAM del servidor antes!",
                "I wish it would stop raining so we could fix the antenna." to "Ojalá dejara de llover para que pudiéramos reparar la antena."
            ),
            mistakes = listOf(
                "I wish I will pass the exam." to "I hope I will pass the exam. (Para deseos futuros posibles usa 'hope', no 'wish')",
                "I wish I would have more money." to "I wish I had more money. (Con 'I', usa Past Simple para presente)"
            ),
            glossary = listOf(
                "If only" to "Estructura más enfática que 'I wish' para expresar deseo.",
                "Annoyance" to "Molestia o queja ante el comportamiento recurrente de alguien."
            )
        )

        // Mod 4: Pasivas y Causativas
        addTopic(
            id = "gram_09",
            title = "Passive Voice in Complex Tenses & Impersonal Reporting Passives",
            titleSpanish = "Voz Pasiva Avanzada y Pasivas Impersonales",
            category = "Gramática",
            moduleGroup = "Módulo 4: Voz Pasiva y Causativas",
            explanation = "1) Pasivas complejas: Present/Past Continuous ('The cables are being replaced'), Present/Past Perfect ('The firewall has been configured'). 2) Pasivas impersonales de reporte para estilo formal y periodístico: 'It is said that...' o 'Subject + is believed / considered / reported + to + infinitivo' ('The new curriculum is believed to improve grades').",
            examples = listOf(
                "The school network is currently being upgraded." to "La red de la escuela está siendo actualizada en este momento.",
                "It is reported that the tech company has launched a new processor." to "Se informa que la compañía tecnológica ha lanzado un nuevo procesador.",
                "He is considered to be one of the best educators in the region." to "Se le considera uno de los mejores educadores de la región."
            ),
            mistakes = listOf(
                "It is believe that..." to "It is believed that... (El verbo siempre va en participio pasado)",
                "The computers are upgrading." to "The computers are being upgraded. (Las computadoras no se actualizan solas)"
            ),
            glossary = listOf(
                "Impersonal passive" to "Estructura para reportar ideas generales de forma objetiva.",
                "Agent" to "Quien realiza la acción (introducido por 'by', frecuentemente omitido si es obvio o desconocido)."
            )
        )

        addTopic(
            id = "gram_10",
            title = "Causative Structures: Have / Get Something Done",
            titleSpanish = "Estructuras Causativas: Have/Get something done",
            category = "Gramática",
            moduleGroup = "Módulo 4: Voz Pasiva y Causativas",
            explanation = "Usamos 'Have / Get + objeto + Participio pasado' cuando pagamos o encargamos a otra persona que haga un servicio por nosotros. 'Have someone do something' (mandar a alguien) o 'Get someone to do something' (convencer a alguien).",
            examples = listOf(
                "I am having my laptop repaired at the support center." to "Me están reparando la laptop en el centro de soporte.",
                "The principal had the classroom painted over the summer." to "El director mandó pintar el aula durante el verano.",
                "I finally got the technician to reconfigure the switch." to "Por fin logré que el técnico reconfigurara el switch."
            ),
            mistakes = listOf(
                "I repaired my car yesterday at the mechanic." to "I had my car repaired yesterday. (Si lo hizo el mecánico, usa causativa)"
            ),
            glossary = listOf(
                "Causative" to "Estructura que expresa que causamos que una acción sea realizada por un tercero."
            )
        )

        // Mod 5: Estilo Indirecto
        addTopic(
            id = "gram_11",
            title = "Reported Speech & Advanced Reporting Verbs",
            titleSpanish = "Estilo Indirecto y Verbos de Reporte Avanzados",
            category = "Gramática",
            moduleGroup = "Módulo 5: Estilo Indirecto",
            explanation = "A nivel B2, superamos el simple 'said/told' usando verbos de reporte precisos: 1) Verb + -ing (admit, deny, suggest): 'He denied breaking the switch'. 2) Verb + to-infinitive (offer, promise, refuse): 'She promised to deliver the grades'. 3) Verb + person + to-inf (warn, advise, remind, persuade): 'The teacher warned us not to cheat'. 4) Verb + that-clause (claim, explain, insist): 'He insisted that the server was secure'.",
            examples = listOf(
                "The technician suggested restarting the modem." to "El técnico sugirió reiniciar el módem.",
                "She advised me to study thirty minutes every day." to "Ella me aconsejó estudiar 30 minutos todos los días.",
                "The director reminded the staff to submit their lesson plans." to "El director recordó al personal entregar sus planeaciones de clase."
            ),
            mistakes = listOf(
                "He suggested me to buy a new laptop." to "He suggested that I buy a new laptop / He suggested buying a new laptop. ('suggest' NUNCA va con 'person + to-inf')",
                "She explained me the rule." to "She explained the rule to me. (Explain requiere preposición 'to' antes de la persona)"
            ),
            glossary = listOf(
                "Reporting verb" to "Verbo que resume la intención comunicativa del hablante original.",
                "Backshift" to "Cambio correlativo de tiempos verbales hacia el pasado en estilo indirecto."
            )
        )

        // Mod 6: Relativas
        addTopic(
            id = "gram_12",
            title = "Relative Clauses: Defining, Non-defining & Prepositions",
            titleSpanish = "Oraciones de Relativo: Especificativas, Explicativas y Preposiciones",
            category = "Gramática",
            moduleGroup = "Módulo 6: Oraciones de Relativo",
            explanation = "1) Defining (sin comas): Información esencial; 'that' puede sustituir a 'who/which', y el pronombre se omite si es objeto ('The book [that] I read'). 2) Non-defining (entre comas): Información extra; NUNCA se usa 'that' y NUNCA se omite el pronombre ('My teacher, who is from London, speaks Spanish'). 3) Formal con preposición: 'The company for which I work' vs informal 'The company I work for'.",
            examples = listOf(
                "The server which crashed yesterday is now running smoothly." to "El servidor que falló ayer ahora está funcionando sin problemas.",
                "Mr. Ramirez, whose daughter studies networking, is our school principal." to "El Sr. Ramírez, cuya hija estudia redes, es el director de nuestra escuela.",
                "The topic about which we were debating is essential for B2." to "El tema sobre el cual estábamos debatiendo es esencial para B2."
            ),
            mistakes = listOf(
                "My brother, that lives in Canada, visited us." to "My brother, who lives in Canada, visited us. (En non-defining jamás uses 'that')",
                "The man who I spoke to him yesterday." to "The man I spoke to yesterday. (No repitas el pronombre 'him')"
            ),
            glossary = listOf(
                "Defining clause" to "Cláusula que identifica de forma indispensable al sujeto u objeto.",
                "Non-defining clause" to "Cláusula que añade información complementaria entre comas."
            )
        )

        // Mod 7: Gerundios e Infinitivos
        addTopic(
            id = "gram_13",
            title = "Gerunds vs. Infinitives with Meaning Change",
            titleSpanish = "Gerundios e Infinitivos con Cambio de Significado",
            category = "Gramática",
            moduleGroup = "Módulo 7: Gerundios e Infinitivos",
            explanation = "Ciertos verbos cambian radicalmente de significado según lleven Gerundio (-ing) o Infinitivo (to + verb): 1) 'Remember/Forget to do' (recordar hacer una tarea futura) vs 'Remember/Forget doing' (recuerdo de un hecho pasado vivido). 2) 'Stop to do' (hacer una pausa para iniciar otra actividad) vs 'Stop doing' (cesar un hábito/acción). 3) 'Try to do' (esforzarse físicamente) vs 'Try doing' (experimentar como método alternativo). 4) 'Regret to say' (lamento informar) vs 'Regret saying' (arrepentirse de lo dicho).",
            examples = listOf(
                "I stopped to drink some water while teaching." to "Hice una pausa para beber agua mientras daba clase.",
                "I stopped drinking soda two years ago." to "Dejé de tomar refresco hace dos años. (Cesar el hábito)",
                "Remember to backup the database every evening." to "Acuérdate de respaldar la base de datos cada tarde.",
                "I distinctly remember installing the driver yesterday." to "Recuerdo claramente haber instalado el controlador ayer."
            ),
            mistakes = listOf(
                "I regret to tell him the bad news yesterday." to "I regretted telling him the bad news. (Para arrepentimiento de un acto pasado usa gerundio)",
                "I tried to turn it on and off, but it still didn't work." to "I tried turning it on and off. (Como experimento/prueba rápida usa gerundio)"
            ),
            glossary = listOf(
                "Bare infinitive" to "Infinitivo sin 'to' (usado tras verbos modales y make/let).",
                "Verbal noun" to "Sustantivo verbal formado con la terminación -ing."
            )
        )

        // Mod 8: Inversión y Énfasis
        addTopic(
            id = "gram_14",
            title = "Emphatic Inversion & Cleft Sentences",
            titleSpanish = "Inversión Enfática y Oraciones Hendidas (Cleft Sentences)",
            category = "Gramática",
            moduleGroup = "Módulo 8: Inversión Enfática y Énfasis",
            explanation = "1) Inversión: Cuando colocamos un adverbio o frase negativa/restrictiva al inicio para dar dramatismo o formalidad alta (formato pregunta auxiliar + sujeto + verbo): 'Never have I seen...', 'Seldom does he...', 'Not only... but also...', 'Under no circumstances should you...'. 2) Cleft Sentences: Dividir una oración para resaltar un elemento específico ('What I really need is more bandwidth' o 'It was the power surge that damaged the motherboard').",
            examples = listOf(
                "Not only did we finish the syllabus, but we also passed the B2 mock exam." to "No solo terminamos el temario, sino que también aprobamos el simulacro B2.",
                "Rarely do students encounter such a clear networking explanation." to "Raras veces los alumnos encuentran una explicación de redes tan clara.",
                "What surprised the teacher was their high level of English fluency." to "Lo que sorprendió al profesor fue su alto nivel de fluidez en inglés.",
                "Under no circumstances must passwords be shared in plain text." to "Bajo ninguna circunstancia se deben compartir contraseñas en texto plano."
            ),
            mistakes = listOf(
                "Never I have seen such a thing." to "Never have I seen such a thing. (La inversión requiere auxiliar antes del sujeto)",
                "Seldom he arrives on time." to "Seldom does he arrive on time. (Requiere 'does' como en una pregunta)"
            ),
            glossary = listOf(
                "Inversion" to "Alteración del orden sujeto-verbo para lograr énfasis formal.",
                "Cleft sentence" to "Estructura dividida (con What o It was) para enfocar la atención."
            )
        )

        // Mod 9: Cuantificadores
        addTopic(
            id = "gram_15",
            title = "Advanced Quantifiers & Distributives (Few, Little, Either, Neither, Each)",
            titleSpanish = "Cuantificadores Avanzados y Distributivos",
            category = "Gramática",
            moduleGroup = "Módulo 9: Cuantificadores y Determinantes",
            explanation = "1) 'Few / Little' (casi nada, sentido negativo) vs 'A few / A little' (algunos, sentido positivo suficiente). 2) 'Either... or' (uno u otro entre 2) vs 'Neither... nor' (ninguno de los 2). 3) 'Each' (cada uno individualmente) vs 'Every' (todos como conjunto global).",
            examples = listOf(
                "I have a few minutes to help you troubleshoot the network." to "Tengo unos cuantos minutos para ayudarte a resolver el problema de red. (Suficiente)",
                "Few students understood the complex recursive algorithm." to "Pocos estudiantes entendieron el complejo algoritmo recursivo. (Casi ninguno)",
                "Neither the switch nor the router was responding to ping requests." to "Ni el switch ni el router respondían a las solicitudes de ping."
            ),
            mistakes = listOf(
                "Neither of them are ready." to "Neither of them is ready. (En inglés formal 'neither' toma verbo en singular)"
            ),
            glossary = listOf(
                "Distributive" to "Palabra que se refiere a los miembros individuales de un grupo.",
                "Uncountable noun" to "Sustantivo no contable (requiere little/much)."
            )
        )

        // Mod 10: Adjetivos y Adverbios
        addTopic(
            id = "gram_16",
            title = "Gradable vs. Non-gradable Adjectives & Royal Order of Adjectives",
            titleSpanish = "Adjetivos Graduables vs. Extremos y Orden de Adjetivos",
            category = "Gramática",
            moduleGroup = "Módulo 10: Adjetivos y Adverbios Avanzados",
            explanation = "1) Graduables (cold, angry, big) se modifican con 'very, fairly, extremely'. No-graduables o Extremos (freezing, furious, huge, essential, dead) se modifican con 'absolutely, completely, totally' (¡NUNCA 'very furious'!). 2) Orden de múltiples adjetivos (OSASCOMP): Opinión -> Tamaño -> Edad -> Forma -> Color -> Origen -> Material -> Propósito + Sustantivo ('A beautiful small old square black Mexican wooden desk').",
            examples = listOf(
                "The server room was absolutely freezing." to "El cuarto de servidores estaba absolutamente helado.",
                "It is absolutely essential to configure firewall rules properly." to "Es absolutamente esencial configurar adecuadamente las reglas del cortafuegos.",
                "He bought an expensive new rectangular black Japanese fiber cable." to "Compró un costoso y nuevo cable de fibra óptico japonés, negro y rectangular."
            ),
            mistakes = listOf(
                "The teacher was very furious." to "The teacher was furious / absolutely furious. ('furious' es extremo, no admite 'very')",
                "A leather black jacket." to "A black leather jacket. (Color va antes de Material)"
            ),
            glossary = listOf(
                "Extreme adjective" to "Adjetivo no graduable que ya contiene el significado 'muy' en su raíz.",
                "OSASCOMP" to "Regla mnemotécnica para el orden natural de los adjetivos en inglés."
            )
        )

        // Mod 11: Conectores
        addTopic(
            id = "gram_17",
            title = "Discourse Markers & Connectors of Contrast, Cause & Addition",
            titleSpanish = "Marcadores del Discurso y Conectores de Contraste y Causa",
            category = "Gramática",
            moduleGroup = "Módulo 11: Conectores y Marcadores del Discurso",
            explanation = "Crucial para B2 Writing y Speaking: 1) Contraste: 'However' (inicio de oración seguido de coma), 'Although / Even though' (conecta dos cláusulas completas), 'Despite / In spite of' (seguido de sustantivo o gerundio -ing, o 'the fact that'). 2) Causa y Consecuencia: 'Consequently, Therefore, As a result, Due to / Owing to + noun/-ing'. 3) Adición formal: 'Furthermore, Moreover, In addition'.",
            examples = listOf(
                "Despite working full time as a teacher, he studies English every night." to "A pesar de trabajar a tiempo completo como maestro, estudia inglés todas las noches.",
                "The bandwidth was limited; however, the video conference ran without lag." to "El ancho de banda era limitado; sin embargo, la videoconferencia funcionó sin retraso.",
                "Owing to a power outage, the computer lab was closed for the afternoon." to "Debido a un corte de energía, el laboratorio de cómputo estuvo cerrado durante la tarde."
            ),
            mistakes = listOf(
                "Despite of the rain, we went out." to "Despite the rain / In spite of the rain. ('despite' nunca lleva 'of')",
                "Although he studied hard, but he failed." to "Although he studied hard, he failed. (No mezcles 'although' y 'but' en la misma oración)"
            ),
            glossary = listOf(
                "Discourse marker" to "Palabra de enlace que guía al lector a través de la lógica del texto.",
                "Cohesion" to "Fluidez y enlace armónico entre ideas y párrafos."
            )
        )

        // Mod 12: Question tags
        addTopic(
            id = "gram_18",
            title = "Advanced Question Tags, Echo Questions & So/Neither do I",
            titleSpanish = "Question Tags Avanzadas, Preguntas Eco y Respuestas Cortas",
            category = "Gramática",
            moduleGroup = "Módulo 12: Question Tags y Respuestas de Acuerdo",
            explanation = "Casos especiales de Question Tags: 1) 'Let's go, shall we?' 2) 'I am right, aren't I?' 3) 'Don't be late, will you?' / 'Help me, would you?' 4) 'Nobody called, did they?' (con pronombres indefinidos el tag usa 'they'). 5) 'So do I / Neither do I / Nor have I' para coincidir en afirmaciones y negaciones.",
            examples = listOf(
                "Let's check the IP configuration, shall we?" to "Revisemos la configuración IP, ¿de acuerdo?",
                "I am responsible for the network security, aren't I?" to "Soy responsable de la seguridad de red, ¿verdad?",
                "Everybody understood the instructions, didn't they?" to "Todos entendieron las instrucciones, ¿verdad?",
                "I haven't installed the software yet. - Neither have I." to "Aún no he instalado el software. - Yo tampoco."
            ),
            mistakes = listOf(
                "I am ready, am not I?" to "I am ready, aren't I? (En inglés estándar se usa aren't I)",
                "I like pizza. - So I do." to "I like pizza. - So do I. (El orden es So + auxiliar + sujeto)"
            ),
            glossary = listOf(
                "Question tag" to "Mini pregunta añadida al final de una afirmación para verificar o buscar acuerdo.",
                "Echo question" to "Pregunta de confirmación que repite con sorpresa lo dicho por el interlocutor."
            )
        )

        // ==========================================
        // 2. VOCABULARIO (15 Campos / 20 Temas)
        // ==========================================

        addTopic(
            id = "vocab_01",
            title = "Workplace & Office Phrasal Verbs",
            titleSpanish = "Phrasal Verbs de Trabajo y Oficina",
            category = "Vocabulario",
            moduleGroup = "Módulo 1: Phrasal Verbs de Trabajo",
            explanation = "Phrasal verbs indispensables en el ambiente profesional: 'carry out' (llevar a cabo/ejecutar), 'take over' (asumir el control/relevar), 'call off' (cancelar), 'put off' (posponer), 'draw up' (redactar un documento formal/contrato), 'step down' (renunciar a un cargo), 'deal with' (lidiar con / atender clientes o problemas).",
            examples = listOf(
                "The IT support team carried out a complete system diagnostic." to "El equipo de soporte de TI llevó a cabo un diagnóstico completo del sistema.",
                "We had to put off the staff meeting until Friday morning." to "Tuvimos que posponer la reunión de personal hasta el viernes por la mañana.",
                "The director drew up a new protocol for remote teaching." to "El director redactó un nuevo protocolo para la enseñanza remota."
            ),
            mistakes = listOf(
                "We cancelled off the meeting." to "We called off the meeting / cancelled the meeting. (No mezcles cancel y call off)"
            ),
            glossary = listOf(
                "Carry out" to "Ejecutar o realizar un experimento, plan o tarea.",
                "Call off" to "Cancelar definitivamente un evento programado."
            )
        )

        addTopic(
            id = "vocab_02",
            title = "Technology, IT & Networking Phrasal Verbs",
            titleSpanish = "Phrasal Verbs de Tecnología, Informática y Redes",
            category = "Vocabulario",
            moduleGroup = "Módulo 2: Phrasal Verbs de Tecnología",
            explanation = "Phrasal verbs técnicos esenciales: 'back up' (hacer copia de seguridad), 'boot up' (iniciar o arrancar el sistema), 'wipe out' (borrar por completo los datos), 'plug in' (conectar a la corriente/puerto), 'log on/in' & 'log off/out' (iniciar/cerrar sesión), 'hack into' (vulnerar un sistema), 'shut down' (apagar ordenadamente).",
            examples = listOf(
                "Always remember to back up your critical files to the cloud." to "Recuerda siempre respaldar tus archivos críticos en la nube.",
                "The server takes around two minutes to boot up completely." to "El servidor tarda alrededor de dos minutos en arrancar completamente.",
                "A malicious script wiped out the temporary database cache." to "Un script malicioso borró por completo la memoria caché temporal de la base de datos."
            ),
            mistakes = listOf(
                "I will backup the files." to "I will back up the files. ('backup' como una sola palabra es sustantivo; el verbo lleva espacio 'back up')"
            ),
            glossary = listOf(
                "Back up" to "Crear un duplicado de seguridad de los datos.",
                "Boot up" to "Cargar el sistema operativo en la memoria RAM."
            )
        )

        addTopic(
            id = "vocab_03",
            title = "Everyday Social & Relationship Phrasal Verbs",
            titleSpanish = "Phrasal Verbs de Relaciones y Vida Cotidiana",
            category = "Vocabulario",
            moduleGroup = "Módulo 3: Phrasal Verbs Cotidianos",
            explanation = "'get along with' (llevarse bien con), 'look up to' (admirar o respetar a alguien), 'put up with' (tolerar o aguantar una molestia), 'run into' (encontrarse por casualidad con alguien), 'fall out with' (pelearse o distanciarse de un amigo), 'make up for' (compensar por algo).",
            examples = listOf(
                "As a teacher, it is vital to get along well with both students and parents." to "Como maestro, es vital llevarse bien tanto con los alumnos como con los padres.",
                "I admire my mentor and always look up to his technical expertise." to "Admiro a mi mentor y siempre respeto su experiencia técnica.",
                "I cannot put up with the continuous server noise anymore." to "Ya no puedo tolerar el ruido continuo del servidor."
            ),
            mistakes = listOf(
                "I met by accident with my friend." to "I ran into my friend. ('run into' es la forma natural y nativa)"
            ),
            glossary = listOf(
                "Put up with" to "Tolerar una situación incómoda sin quejarse.",
                "Look up to" to "Tener a alguien como modelo a seguir."
            )
        )

        addTopic(
            id = "vocab_04",
            title = "Essential Collocations with Do, Make, Take & Have",
            titleSpanish = "Colocaciones Esenciales con Do, Make, Take y Have",
            category = "Vocabulario",
            moduleGroup = "Módulo 4: Colocaciones con Verbos Clave",
            explanation = "1) MAKE: 'make a decision, make a mistake, make progress, make an effort, make a living'. 2) DO: 'do research, do business, do a favor, do your best, do homework'. 3) TAKE: 'take into account/consideration, take advantage of, take notes, take place, take a break'. 4) HAVE: 'have an impact, have a conversation, have doubts, have a look'.",
            examples = listOf(
                "We need to do some research before upgrading the school network." to "Necesitamos investigar antes de actualizar la red escolar.",
                "The technician made a great effort to recover the lost partition." to "El técnico hizo un gran esfuerzo para recuperar la partición perdida.",
                "Please take into account that the bandwidth is shared among all classrooms." to "Por favor toma en cuenta que el ancho de banda se comparte entre todas las aulas."
            ),
            mistakes = listOf(
                "I made my homework." to "I did my homework. (Las tareas académicas siempre van con 'do')",
                "I did a mistake in the exam." to "I made a mistake in the exam. (Los errores van con 'make')"
            ),
            glossary = listOf(
                "Collocation" to "Combinación habitual y natural de dos o más palabras que suelen aparecer juntas en el idioma.",
                "Fixed expression" to "Frase con estructura fija aceptada por los hablantes nativos."
            )
        )

        addTopic(
            id = "vocab_05",
            title = "Advanced B2 Adjective + Noun Collocations",
            titleSpanish = "Colocaciones Adjetivo + Sustantivo de Nivel B2",
            category = "Vocabulario",
            moduleGroup = "Módulo 5: Colocaciones Avanzadas",
            explanation = "Combinaciones léxicas que diferencian el nivel B2: 'vital role' (papel fundamental), 'bitter disappointment' (amarga decepción), 'heated debate' (acalorado debate), 'widespread belief' (creencia generalizada), 'heavy traffic/rain' (tráfico pesado/lluvia torrencial), 'narrow escape' (escape por poco), 'flawed logic' (lógica defectuosa).",
            examples = listOf(
                "Educational technology plays a vital role in modern secondary schools." to "La tecnología educativa juega un papel fundamental en las escuelas secundarias modernas.",
                "The failure of the backup system was a bitter disappointment for the IT department." to "La falla del sistema de respaldo fue una amarga decepción para el departamento de TI.",
                "There is a widespread belief that learning English opens international remote job opportunities." to "Existe la creencia generalizada de que aprender inglés abre oportunidades de trabajo remoto internacional."
            ),
            mistakes = listOf(
                "Strong rain fell all day." to "Heavy rain fell all day. (Para lluvia se usa 'heavy', no 'strong')"
            ),
            glossary = listOf(
                "Vital role" to "Función indispensable o de suma trascendencia.",
                "Bitter disappointment" to "Decepción profunda y dolorosa."
            )
        )

        addTopic(
            id = "vocab_06",
            title = "Professional & Workplace Idioms",
            titleSpanish = "Modismos e Idioms del Ámbito Laboral y Profesional",
            category = "Vocabulario",
            moduleGroup = "Módulo 6: Idioms del Trabajo",
            explanation = "Idioms clave en reuniones de trabajo y proyectos: 'hit the ground running' (empezar un proyecto con ritmo y éxito inmediato), 'cut corners' (ahorrar tiempo o dinero haciendo las cosas con mala calidad), 'think outside the box' (pensar de forma creativa e innovadora), 'on the same page' (estar de acuerdo o alineados), 'burn the candle at both ends' (trabajar en exceso de sol a sol), 'back to the drawing board' (volver a empezar desde cero porque el plan falló).",
            examples = listOf(
                "The new teacher hit the ground running with an interactive English workshop." to "El nuevo maestro empezó con el pie derecho y con gran ritmo con un taller interactivo de inglés.",
                "We cannot cut corners when it comes to network cabling and firewall security." to "No podemos escatimar ni hacer las cosas mal cuando se trata del cableado de red y la seguridad del cortafuegos.",
                "Let's review the schedule so that everyone is on the same page." to "Revisemos el cronograma para que todos estemos en el mismo canal / sintonía."
            ),
            mistakes = listOf(
                "Think out of the box." to "Think outside the box. (La preposición correcta es 'outside')",
                "We are in the same page." to "We are on the same page. (La preposición fija es 'on')"
            ),
            glossary = listOf(
                "Hit the ground running" to "Comenzar con energía y productividad desde el primer día.",
                "Cut corners" to "Tomar atajos que comprometen la calidad."
            )
        )

        addTopic(
            id = "vocab_07",
            title = "Idioms for Emotions, Opinions & Decisions",
            titleSpanish = "Modismos para Expresar Emociones y Opiniones",
            category = "Vocabulario",
            moduleGroup = "Módulo 7: Idioms de Emociones",
            explanation = "'over the moon' (extremadamente feliz), 'down in the dumps' (desanimado o triste), 'see eye to eye' (coincidir plenamente en opinión), 'bite the bullet' (afrontar una situación difícil con valentía), 'take it with a pinch of salt' (tomar algo con reserva/escepticismo), 'once in a blue moon' (muy rara vez).",
            examples = listOf(
                "I was over the moon when my students achieved their B2 English certificates." to "Estaba en las nubes de alegría cuando mis alumnos obtuvieron sus certificados de inglés B2.",
                "The administration and the tech team do not always see eye to eye on software budgets." to "La administración y el equipo técnico no siempre coinciden en opinión sobre el presupuesto de software.",
                "I decided to bite the bullet and replace the faulty main power supply." to "Decidí hacer de tripas corazón y reemplazar la fuente de poder principal averiada."
            ),
            mistakes = listOf(
                "I am in the moon." to "I am over the moon. (La expresión exacta es 'over the moon')"
            ),
            glossary = listOf(
                "Bite the bullet" to "Aceptar una dificultad inevitable y superarla con determinación.",
                "Pinch of salt" to "Grano de sal (escepticismo sano)."
            )
        )

        addTopic(
            id = "vocab_08",
            title = "Computer Hardware, Networks & IT Support Terminology",
            titleSpanish = "Vocabulario de Hardware, Redes y Soporte Técnico",
            category = "Vocabulario",
            moduleGroup = "Módulo 8: Informática y Redes",
            explanation = "Términos técnicos en inglés indispensables para soporte: 'bandwidth' (ancho de banda), 'troubleshoot' (diagnosticar y resolver fallas), 'motherboard' (placa madre), 'deployment' (despliegue de software/sistemas), 'encryption' (cifrado), 'downtime' (tiempo de inactividad/caída del sistema), 'latency' (latencia), 'packet loss' (pérdida de paquetes), 'gateway' (puerta de enlace), 'throughput' (rendimiento de transferencia).",
            examples = listOf(
                "We need to troubleshoot the high latency on the secondary Wi-Fi access point." to "Necesitamos diagnosticar y resolver la alta latencia en el punto de acceso Wi-Fi secundario.",
                "The scheduled server maintenance resulted in only five minutes of downtime." to "El mantenimiento programado del servidor resultó en solo cinco minutos de inactividad.",
                "End-to-end encryption ensures that sensitive student records remain confidential." to "El cifrado de extremo a extremo garantiza que los expedientes sensibles de los alumnos permanezcan confidenciales."
            ),
            mistakes = listOf(
                "I am doing a troubleshooting." to "I am troubleshooting the issue. ('troubleshoot' se conjuga directamente como verbo)"
            ),
            glossary = listOf(
                "Troubleshoot" to "Investigar sistemáticamente la causa raíz de un problema técnico.",
                "Throughput" to "Volumen real de datos procesados por unidad de tiempo."
            )
        )

        addTopic(
            id = "vocab_09",
            title = "Education, Pedagogy & Classroom Management",
            titleSpanish = "Vocabulario de Educación, Pedagogía y Aula",
            category = "Vocabulario",
            moduleGroup = "Módulo 9: Educación y Pedagogía",
            explanation = "Vocabulario para docentes: 'curriculum' (plan de estudios), 'assessment' (evaluación), 'peer review' (evaluación entre pares), 'syllabus' (programa temático de la materia), 'classroom management' (gestión del aula y disciplina), 'lifelong learning' (aprendizaje continuo de por vida), 'formative vs summative evaluation' (evaluación formativa vs sumativa), 'scaffolding' (andamiaje pedagógico).",
            examples = listOf(
                "Formative assessment allows teachers to adapt instruction according to student needs." to "La evaluación formativa permite a los maestros adaptar la instrucción según las necesidades de los alumnos.",
                "Effective classroom management fosters an engaging and respectful learning environment." to "Una gestión eficaz del aula fomenta un ambiente de aprendizaje participativo y respetuoso.",
                "The telesecundaria curriculum integrates digital video resources with collaborative projects." to "El plan de estudios de telesecundaria integra recursos de video digital con proyectos colaborativos."
            ),
            mistakes = listOf(
                "I need to do an exam to my students." to "I need to test my students / give my students an exam. (Los maestros 'give/set an exam', los alumnos 'take an exam')"
            ),
            glossary = listOf(
                "Scaffolding" to "Estructuras de apoyo temporal que el docente brinda al estudiante para construir nuevos conocimientos.",
                "Formative assessment" to "Evaluación continua orientada a mejorar el aprendizaje en proceso."
            )
        )

        addTopic(
            id = "vocab_10",
            title = "Word Formation: B2 Prefixes & Suffixes",
            titleSpanish = "Formación de Palabras: Prefijos y Sufijos B2",
            category = "Vocabulario",
            moduleGroup = "Módulo 10: Formación de Palabras",
            explanation = "Indispensable para el examen Cambridge Use of English Part 3: 1) Sustantivos a partir de verbos/adjetivos: -ment (development), -tion (instruction), -ness (awareness), -ity (flexibility), -hood (neighborhood), -ship (relationship). 2) Adjetivos: -able/-ible (reliable), -ful (helpful), -less (careless), -ous (cautious). 3) Prefijos negativos y modificadores: un- (unreliable), in-/im-/il-/ir- (insecure, impossible, illiterate, irregular), dis- (disconnect), mis- (misunderstand), over- (overheat), under- (underestimate).",
            examples = listOf(
                "The sudden development of artificial intelligence has transformed education." to "El repentino desarrollo de la inteligencia artificial ha transformado la educación.",
                "Due to a misunderstanding, the backup was saved in the wrong directory." to "Debido a un malentendido, el respaldo se guardó en el directorio equivocado.",
                "His reliability as a network administrator is widely acknowledged." to "Su confiabilidad como administrador de redes es ampliamente reconocida."
            ),
            mistakes = listOf(
                "Unresponsible." to "Irresponsible. (El prefijo negativo para 'responsible' es 'ir-')",
                "Dispatient." to "Impatient. (Para palabras que empiezan con 'p' suele usarse 'im-')"
            ),
            glossary = listOf(
                "Affix" to "Morfema que se añade a una raíz léxica (prefijo o sufijo).",
                "Stem / Root" to "Raíz léxica base sobre la cual se forman derivados."
            )
        )

        addTopic(
            id = "vocab_11",
            title = "Critical False Friends for Spanish Speakers",
            titleSpanish = "Falsos Amigos Críticos para Hispanohablantes",
            category = "Vocabulario",
            moduleGroup = "Módulo 11: Falsos Amigos",
            explanation = "Palabras engañosas que parecen español pero significan algo muy diferente: 1) 'Actually' = en realidad/de hecho (NO actualmente -> currently/nowadays). 2) 'Assist' = ayudar (NO asistir a un evento -> attend). 3) 'Realize' = darse cuenta (NO realizar una tarea -> carry out/perform). 4) 'Sensible' = sensato/prudente (NO sensible emocionalmente -> sensitive). 5) 'Embarrassed' = apenado/avergonzado (NO embarazada -> pregnant). 6) 'Notice' = notar/fijarse (NO dar noticias -> news).",
            examples = listOf(
                "I didn't realize that the router was unplugged." to "No me di cuenta de que el router estaba desconectado.",
                "All teachers must attend the pedagogical meeting tomorrow." to "Todos los maestros deben asistir a la junta pedagógica mañana.",
                "It is sensible to keep multiple offsite backups." to "Es sensato y prudente mantener múltiples respaldos fuera del sitio."
            ),
            mistakes = listOf(
                "I am working actually in a school." to "I am currently working in a school. ('Actually' significa en realidad)",
                "I assisted to the technology conference." to "I attended the technology conference. ('Assist' significa brindar ayuda)"
            ),
            glossary = listOf(
                "False cognate" to "Palabra de ortografía semejante en dos idiomas pero con significados dispares.",
                "Currently" to "En la actualidad, en este momento."
            )
        )

        // ==========================================
        // 3. LISTENING (8 Módulos)
        // ==========================================

        addTopic(
            id = "list_01",
            title = "Gist vs. Specific Detail in Academic & Tech Podcasts",
            titleSpanish = "Idea Global vs. Detalles Específicos en Audios",
            category = "Listening",
            moduleGroup = "Módulo 1: Estrategias de Escucha",
            explanation = "1) Escucha global (Gist): Comprender el tema principal, la conclusión y el propósito del audio sin detenerse en cada palabra desconocida. 2) Escucha de detalle (Specific info): Identificar números, fechas, nombres propios, causas y efectos específicos respondiendo a las preguntas Who, When, Why, How.",
            examples = listOf(
                "Audio excerpt: 'Although fiber optics offer superior speeds, their installation cost remains prohibitive in remote rural zones.'" to "Idea principal: El costo limita el despliegue de fibra óptica en zonas rurales.",
                "Audio question: 'What is the main obstacle mentioned?' -> Answer: Financial cost / budget." to "Pregunta de comprensión de audio enfocada en el detalle determinante."
            ),
            mistakes = listOf(
                "Intentar traducir mentalmente cada palabra" to "Enfócate en las palabras con carga semántica (sustantivos, verbos de acción, adjetivos)"
            ),
            glossary = listOf(
                "Gist" to "Esencia o idea central de un discurso oral.",
                "Distractor" to "Opción trampa en preguntas de opción múltiple que menciona palabras del audio pero con sentido alterado."
            )
        )

        addTopic(
            id = "list_02",
            title = "Detecting Speaker Attitude, Tone & Hidden Purpose",
            titleSpanish = "Detectar Actitud, Tono e Intención del Hablante",
            category = "Listening",
            moduleGroup = "Módulo 2: Inferencia Auditiva",
            explanation = "A nivel B2, el mensaje no siempre es explícito. Debes inferir si el hablante está satisfecho, escéptico, impaciente o entusiasta mediante la entonación ascendente/descendente, el énfasis en palabras clave (pitch peak) y expresiones de reserva como 'Well, to be fair...', 'I suppose so, but...'.",
            examples = listOf(
                "'I was expecting a bit more support from the technical vendor, frankly.'" to "Tono: Decepción cortés pero evidente.",
                "'Surely there must be a more efficient way to calculate subnet masks!'" to "Actitud: Frustración y búsqueda de alternativas."
            ),
            mistakes = listOf(
                "Asumir que un tono amable implica acuerdo total" to "Presta atención a los giros introducidos por 'however' o 'nonetheless' en el audio"
            ),
            glossary = listOf(
                "Inference" to "Deducción lógica de lo no dicho explícitamente.",
                "Intonation contour" to "Curva melodiosa de la voz que transmite estados anímicos."
            )
        )

        addTopic(
            id = "list_03",
            title = "Mastering Accents: British, North American & Global Englishes",
            titleSpanish = "Acentos Principales: Británico, Norteamericano y Global",
            category = "Listening",
            moduleGroup = "Módulo 3: Variedades de Acento",
            explanation = "Diferencias fonéticas clave: 1) Acento rótico (General American) donde la 'r' siempre se pronuncia ('car', 'hard') vs no rótico (Received Pronunciation británico) donde la 'r' solo suena ante vocal. 2) Flap T americano ('water', 'better' suenan casi como 'd/r suave') vs T oclusiva o glotal británica. 3) Vocales cortas como la 'a' en 'bath, can't' (/æ/ americano vs /ɑː/ británico).",
            examples = listOf(
                "American: /wɑːtər/ vs British: /wɔːtə/" to "Pronunciación de 'water' en ambas variantes.",
                "Schedule: American /skedʒuːl/ vs British /ˈʃedjuːl/" to "Diferencia léxico-fonética clásica."
            ),
            mistakes = listOf(
                "Estudiar solo con acento estadounidense" to "Exponte regularmente a acentos de Reino Unido, Australia y hablantes no nativos profesionales"
            ),
            glossary = listOf(
                "Rhoticity" to "Pronunciación clara del fonema /r/ tras vocal.",
                "Glottal stop" to "Oclusión en las cuerdas vocales que reemplaza al sonido /t/ en inglés británico informal."
            )
        )

        // ==========================================
        // 4. SPEAKING (10 Módulos)
        // ==========================================

        addTopic(
            id = "speak_01",
            title = "Comparing & Contrasting Two Visual Situations (Cambridge Part 2)",
            titleSpanish = "Comparar y Contrastar Dos Situaciones Visuales",
            category = "Speaking",
            moduleGroup = "Módulo 1: Comparación Visual",
            explanation = "En la Parte 2 del Speaking B2, debes hablar 1 minuto ininterrumpido comparando 2 fotos. Estructura recomendada: 1) Frase de apertura ('Both pictures show people engaging with technology in distinct settings...'). 2) Semejanzas ('In both images, concentration is key...'). 3) Contrastes clave ('Whereas in the first picture the student is working autonomously, in the second one there is a collaborative dynamic...'). 4) Especulación sobre sentimientos ('They might be feeling relieved because...').",
            examples = listOf(
                "Whereas the technician in the first photo is working on hardware, the person in the second one is delivering an online class." to "Mientras que el técnico en la primera foto trabaja en hardware, la persona en la segunda está dando una clase en línea.",
                "Both scenarios highlight the essential role of digital tools in contemporary education." to "Ambos escenarios destacan el papel esencial de las herramientas digitales en la educación contemporánea."
            ),
            mistakes = listOf(
                "Describir la foto 1 y luego la foto 2 de forma aislada" to "Compara y contrasta constantemente usando 'whereas, on the other hand, in contrast, both'"
            ),
            glossary = listOf(
                "Whereas" to "Mientras que (conector de contraste elegante).",
                "Speculate" to "Emitir hipótesis con 'might be, could be, appears to be'."
            )
        )

        addTopic(
            id = "speak_02",
            title = "Collaborative Task: Negotiating, Agreeing & Disagreeing Diplomatically",
            titleSpanish = "Tarea Colaborativa y Negociación Diplomática",
            category = "Speaking",
            moduleGroup = "Módulo 2: Interacción y Negociación",
            explanation = "Frases indispensables para debatir con un compañero: 1) Iniciar / Preguntar opinión: 'What are your thoughts on this?', 'Shall we start with...?'. 2) Acuerdo total: 'I couldn't agree more', 'You have hit the nail on the head'. 3) Acuerdo parcial / Desacuerdo diplomático: 'I see your point, but don't you think that...?', 'That is a valid argument, however...'. 4) Llegar a una decisión final: 'So, which two options shall we settle on?'.",
            examples = listOf(
                "I take your point about budget constraints; nonetheless, providing reliable internet access should be our top priority." to "Entiendo tu punto sobre las restricciones de presupuesto; no obstante, brindar internet confiable debería ser nuestra prioridad máxima.",
                "Would you agree that hands-on workshops are more effective than purely theoretical lectures?" to "¿Estarías de acuerdo en que los talleres prácticos son más efectivos que las clases puramente teóricas?"
            ),
            mistakes = listOf(
                "Decir 'I disagree with you' de forma cortante" to "Suaviza el desacuerdo con 'I am not entirely sure about that' o 'I see what you mean, but...'"
            ),
            glossary = listOf(
                "Turn-taking" to "Alternancia fluida del uso de la palabra sin monopolizar la conversación.",
                "Consensus" to "Acuerdo conjunto tras sopesar diferentes opciones."
            )
        )

        addTopic(
            id = "speak_03",
            title = "Fluency Fillers, Paraphrasing & Self-Correction Techniques",
            titleSpanish = "Muletillas de Fluidez, Paráfrasis y Autocorrección",
            category = "Speaking",
            moduleGroup = "Módulo 3: Fluidez Conversacional",
            explanation = "Cuando se te olvide una palabra o necesites estructurar tu pensamiento, NO te quedes en silencio absoluto: 1) Ganar tiempo: 'Well, let me see...', 'That is quite a thought-provoking question...', 'What I mean by that is...'. 2) Parafrasear: 'It is a device that allows you to...', 'It is similar to...'. 3) Autocorregirse con naturalidad: '..., or rather, ...', '..., what I meant to say was...'.",
            examples = listOf(
                "It is a kind of network device used for filtering incoming data packets—a firewall, that's the word." to "Es una especie de dispositivo de red usado para filtrar paquetes entrantes—un cortafuegos, esa es la palabra.",
                "Well, looking at the situation from a teacher's perspective, I would argue that..." to "Bueno, viendo la situación desde la perspectiva de un docente, sostendría que..."
            ),
            mistakes = listOf(
                "Quedarse callado 5 segundos diciendo 'eeeeh...'" to "Usa expresiones puente como 'To put it another way...' o 'What I am trying to say is...'"
            ),
            glossary = listOf(
                "Paraphrase" to "Explicar un concepto con palabras alternativas cuando no recuerdas el término exacto.",
                "Spontaneous speech" to "Discurso oral natural sin memorización rígida."
            )
        )

        // ==========================================
        // 5. READING (8 Módulos)
        // ==========================================

        addTopic(
            id = "read_01",
            title = "Skimming & Scanning in Technical Manuals & Articles",
            titleSpanish = "Skimming y Scanning en Textos Técnicos y Artículos",
            category = "Reading",
            moduleGroup = "Módulo 1: Técnicas de Lectura Rápida",
            explanation = "1) Skimming: Lectura veloz de títulos, subtítulos, primera y última oración de cada párrafo para captar la arquitectura temática general en menos de 60 segundos. 2) Scanning: Rastreo ocular de elementos gráficos concretos (números, códigos, siglas, nombres propios) para contestar una pregunta puntual sin leer el texto completo.",
            examples = listOf(
                "Scanning question: 'What is the recommended MTU size for VLAN tagging?' -> Look directly for digits followed by 'MTU'." to "Técnica de escaneo directo para responder preguntas técnicas con rapidez.",
                "Skimming a 500-word educational article: Read intro + topic sentences to summarize the main hypothesis." to "Lectura veloz para identificar la tesis del autor."
            ),
            mistakes = listOf(
                "Leer palabra por palabra desde el inicio antes de leer las preguntas" to "Lee primero las preguntas para saber exactamente qué buscar mediante scanning"
            ),
            glossary = listOf(
                "Topic sentence" to "Oración principal que condensa la idea central de un párrafo.",
                "Keyword scanning" to "Búsqueda focalizada de palabras clave en el cuerpo del texto."
            )
        )

        addTopic(
            id = "read_02",
            title = "Gapped Text & Cohesive Reference Links (Cambridge Part 6)",
            titleSpanish = "Texto con Huecos y Nexos de Cohesión (Gapped Text)",
            category = "Reading",
            moduleGroup = "Módulo 2: Cohesión Textual",
            explanation = "En la Parte 6 del Cambridge B2, debes insertar oraciones eliminadas en un texto. Claves de resolución: 1) Pronombres anafóricos ('This, these, such discoveries, he, it'). 2) Conectores de contraste o causa antes y después del hueco ('On the other hand, consequently'). 3) Continuidad cronológica o de significado.",
            examples = listOf(
                "Previous sentence mentions: 'The team encountered an unexpected routing loop.' -> The missing sentence must explain: 'This issue caused the primary gateway to reboot repeatedly.'" to "Identificación del pronombre 'This issue' que conecta con 'routing loop'."
            ),
            mistakes = listOf(
                "Elegir la oración solo porque contiene una palabra repetida" to "Verifica que el flujo lógico antes Y después del espacio encaje a la perfección"
            ),
            glossary = listOf(
                "Anaphoric reference" to "Palabra o pronombre que remite a una idea mencionada con anterioridad.",
                "Cohesive tie" to "Lazo lingüístico que une oraciones contiguas."
            )
        )

        // ==========================================
        // 6. WRITING (9 Módulos)
        // ==========================================

        addTopic(
            id = "write_01",
            title = "Structure of the B2 Formal Opinion Essay",
            titleSpanish = "Estructura del Ensayo Formal de Opinión B2",
            category = "Writing",
            moduleGroup = "Módulo 1: Ensayos Académicos",
            explanation = "Estructura de 4-5 párrafos (140-190 palabras): 1) Introducción: Presentar el tema con paráfrasis + Tesis clara. 2) Párrafo del Punto 1: Idea + Justificación + Ejemplo. 3) Párrafo del Punto 2: Idea + Justificación + Ejemplo. 4) Párrafo del Punto 3 (propio): Idea adicional innovadora. 5) Conclusión: Resumen conciso de argumentos reafirmando la postura sin introducir datos nuevos. Registro 100% formal (cero contracciones, sin jerga).",
            examples = listOf(
                "Introduction example: 'In today's digital age, the integration of digital devices in secondary education has sparked substantial debate. While some argue that screens cause distractions, it is widely believed that technology enhances student engagement and prepares them for future careers.'" to "Introducción formal con contraste y tesis clara.",
                "Conclusion example: 'Taking all these factors into account, it can be concluded that integrating technology into classroom teaching yields far more advantages than drawbacks, provided that clear pedagogical guidelines are established.'" to "Párrafo de cierre formal y estructurado."
            ),
            mistakes = listOf(
                "Usar contracciones como 'don't, can't, it's' en un ensayo formal" to "Escribe siempre las formas completas: 'do not, cannot, it is'",
                "Usar lenguaje excesivamente informal como 'a lot of, stuff, kids'" to "Sustituye por 'a substantial number of, equipment/elements, students/pupils'"
            ),
            glossary = listOf(
                "Thesis statement" to "Oración que resume la postura y propósito principal del ensayo.",
                "Formal register" to "Nivel de lenguaje objetivo, sin contracciones ni expresiones coloquiales."
            )
        )

        addTopic(
            id = "write_02",
            title = "Formal & Informal Email Writing for Education & Support",
            titleSpanish = "Redacción de Correos Formales e Informales",
            category = "Writing",
            moduleGroup = "Módulo 2: Correspondencia Escrita",
            explanation = "1) Formal (a directores, clientes, soporte internacional): Saludo: 'Dear Mr. Smith / Dear Sir or Madam'. Apertura: 'I am writing to inquire about / to bring to your attention...'. Cierre: 'I look forward to hearing from you. Yours sincerely (si conoces el nombre) / Yours faithfully (si no conoces el nombre)'. 2) Informal (a colegas y amigos): 'Hi Mark, Hope you're doing well! Just wanted to check... Cheers / Best wishes'.",
            examples = listOf(
                "Dear Principal Johnson, I am writing to formally request the acquisition of five additional network switches for the computer laboratory." to "Apertura de correo formal a un directivo.",
                "Should you require any further technical specifications, please do not hesitate to contact me." to "Fórmula de cortesía formal antes de la despedida."
            ),
            mistakes = listOf(
                "Terminar con 'Yours sincerely' cuando empezaste con 'Dear Sir or Madam'" to "Si inicias con 'Dear Sir or Madam' debes cerrar con 'Yours faithfully'"
            ),
            glossary = listOf(
                "Yours sincerely" to "Atentamente (cuando conoces el apellido del destinatario).",
                "Yours faithfully" to "Le saluda atentamente (cuando no conoces el nombre del destinatario)."
            )
        )

        addTopic(
            id = "write_03",
            title = "Writing a Technical & Pedagogical Report with Headings",
            titleSpanish = "Redacción de Informes con Encabezados (Report)",
            category = "Writing",
            moduleGroup = "Módulo 3: Informes Técnicos",
            explanation = "Un Report debe tener título claro y subtítulos para cada sección: 1) 'Introduction: The aim of this report is to evaluate...'. 2) 'Current Infrastructure: ...'. 3) 'Areas for Improvement: ...'. 4) 'Recommendations: I would strongly recommend implementing...'. Estilo impersonal y uso de sugerencias condicionales.",
            examples = listOf(
                "Title: Report on the Digital Infrastructure of the Telesecundaria\nIntroduction: The purpose of this report is to outline the current state of classroom technology and propose actionable upgrades." to "Encabezado y objetivo formal de un informe."
            ),
            mistakes = listOf(
                "Escribir un informe sin encabezados ni títulos de sección" to "Organiza siempre el informe en secciones claramente rotuladas"
            ),
            glossary = listOf(
                "Heading" to "Título o subtítulo que delimita una sección del reporte.",
                "Actionable" to "Propuesta concreta y ejecutable."
            )
        )

        // ==========================================
        // 7. PRONUNCIACIÓN (8 Módulos)
        // ==========================================

        addTopic(
            id = "pron_01",
            title = "The Schwa Sound /ə/ & English Vowel Reductions",
            titleSpanish = "El Sonido Schwa /ə/ y Reducción Vocálica",
            category = "Pronunciación",
            moduleGroup = "Módulo 1: Fonética y Vocales",
            explanation = "El sonido más frecuente en inglés es el Schwa /ə/ (un sonido neutro, relajado y breve emitido en sílabas no acentuadas). Ejemplos: 'about' /əˈbaʊt/, 'computer' /kəmˈpjuːtə/, 'teacher' /ˈtiːtʃə/, 'support' /səˈpɔːt/. Los hispanohablantes suelen pronunciar las vocales con la misma fuerza que en español; dominar el Schwa transforma tu fluidez y comprensión auditiva.",
            examples = listOf(
                "about /əˈbaʊt/ — La 'a' inicial no suena como la 'a' española, sino como un murmullo neutro relajado." to "Ejemplo de schwa inicial.",
                "photograph /ˈfəʊtəɡrɑːf/ vs photographer /fəˈtɒɡrəfə/" to "Demostración de cómo el acento léxico cambia las vocales a schwa."
            ),
            mistakes = listOf(
                "Pronunciar 'computer' como /kom-piu-ter/" to "Pronuncia la primera sílaba relajada como /kəm-/"
            ),
            glossary = listOf(
                "Schwa /ə/" to "Vocal media central neutra no acentuada característica del inglés.",
                "Vowel reduction" to "Debilitamiento de vocales en sílabas átonas hacia el sonido schwa."
            )
        )

        addTopic(
            id = "pron_02",
            title = "Regular Past -ED Endings (/t/, /d/, /ɪd/)",
            titleSpanish = "Terminaciones de Pasado Regular -ED",
            category = "Pronunciación",
            moduleGroup = "Módulo 2: Consonantes y Terminaciones",
            explanation = "Regla de oro: 1) Si el verbo termina en sonido /t/ o /d/, se pronuncia como una sílaba extra /ɪd/: 'started, needed, disconnected, wanted'. 2) Si termina en consonante sorda (/p, k, f, s, ʃ, tʃ/), suena como /t/: 'helped, worked, fixed, watched, laughed'. 3) Si termina en consonante sonora o vocal (/b, g, v, z, l, m, n, r/ + vocales), suena como /d/: 'played, repaired, configured, opened'. ¡Solo añade sílaba con T y D!",
            examples = listOf(
                "configured /kənˈfɪɡəd/ (termina en sonido /d/, 3 sílabas)" to "Sonido sonoro /d/",
                "connected /kəˈnektɪd/ (termina en sonido /ɪd/, 3 sílabas)" to "Sonido extra /ɪd/",
                "stopped /stɒpt/ (termina en sonido /t/, 1 sola sílaba)" to "Sonido sordo /t/"
            ),
            mistakes = listOf(
                "Pronunciar 'helped' como 'jel-ped' con dos sílabas" to "Pronúncialo como una sola sílaba /helpt/",
                "Pronunciar 'stopped' como 'estop-ed'" to "Pronúncialo como /stɒpt/"
            ),
            glossary = listOf(
                "Voiced sound" to "Sonido con vibración de cuerdas vocales (/b, d, g, v, z.../).",
                "Voiceless sound" to "Sonido sordo sin vibración de cuerdas vocales (/p, t, k, f, s.../)."
            )
        )

        addTopic(
            id = "pron_03",
            title = "Voiced vs. Voiceless TH Sounds (/θ/ vs. /ð/)",
            titleSpanish = "Sonidos TH Sordo /θ/ y Sonoro /ð/",
            category = "Pronunciación",
            moduleGroup = "Módulo 3: Sonidos Difíciles",
            explanation = "1) Sordo /θ/ (como la 'z' en español de España): 'think, thanks, method, bath, three, both'. 2) Sonoro /ð/ (con vibración y voz, lengua entre los dientes): 'this, that, these, there, mother, although, with'. No sustituyas /θ/ por 's' o 'f', ni /ð/ por 'd'.",
            examples = listOf(
                "think /θɪŋk/ vs sink /sɪŋk/" to "Par mínimo crucial para evitar malentendidos.",
                "they /ðeɪ/ vs day /deɪ/" to "Diferenciación entre el sonido interdental /ð/ y la oclusiva /d/."
            ),
            mistakes = listOf(
                "Decir 'I sink so' en lugar de 'I think so'" to "Coloca suavemente la punta de la lengua entre los dientes frontales"
            ),
            glossary = listOf(
                "Dental fricative" to "Sonido producido con la lengua en contacto con los dientes frontales.",
                "Minimal pair" to "Dos palabras que difieren en un único fonema pero tienen significados distintos."
            )
        )

        // ==========================================
        // 8. FUNCIONES COMUNICATIVAS (8 Módulos)
        // ==========================================

        addTopic(
            id = "func_01",
            title = "Giving & Asking for Advice: Direct vs. Tactful Indirect Forms",
            titleSpanish = "Dar y Pedir Consejos: Fórmulas Directas e Indirectas",
            category = "Funciones Comunicativas",
            moduleGroup = "Módulo 1: Asesoramiento y Sugerencias",
            explanation = "1) Fórmulas directas (entre colegas cercanos): 'You should...', 'You ought to...', 'You had better + infinitivo' (con matiz de urgencia/consecuencia negativa). 2) Fórmulas diplomáticas e indirectas (con directivos o clientes): 'If I were in your shoes, I would...', 'Have you considered updating the firmware?', 'It might be advisable to...', 'One option worth exploring is...'.",
            examples = listOf(
                "You had better create a system restore point before proceeding with the update." to "Más vale que crees un punto de restauración antes de proceder con la actualización.",
                "Have you thought about running an Ethernet cable to avoid Wi-Fi signal interference?" to "¿Has pensado en tender un cable Ethernet para evitar la interferencia de señal Wi-Fi?"
            ),
            mistakes = listOf(
                "You had better to backup your files." to "You had better back up your files. ('had better' va seguido de infinitivo sin 'to')"
            ),
            glossary = listOf(
                "Tact" to "Diplomacia y cortesía al sugerir cambios sin ofender.",
                "Had better" to "Estructura para advertir de consecuencias indeseables si no se sigue el consejo."
            )
        )

        addTopic(
            id = "func_02",
            title = "Making Complaints & Demanding Action with Firm Politeness",
            titleSpanish = "Hacer Quejas y Reclamaciones con Firmeza y Educación",
            category = "Funciones Comunicativas",
            moduleGroup = "Módulo 2: Reclamaciones y Quejas",
            explanation = "Cómo quejarse eficazmente en inglés profesional sin perder la compostura: 1) Introducir el problema: 'I am writing to express my dissatisfaction with...', 'I am afraid there appears to be an issue with...'. 2) Explicar el impacto: 'This has caused considerable disruption to our classroom schedule'. 3) Exigir solución: 'I would appreciate it if you could replace the faulty unit promptly', 'I must insist on a full refund or an immediate replacement'.",
            examples = listOf(
                "I am afraid the internet connection provided does not match the contracted bandwidth specifications." to "Me temo que la conexión a internet proporcionada no coincide con las especificaciones de ancho de banda contratadas.",
                "I would be grateful if you could arrange for a technician to visit the school as a matter of urgency." to "Le agradecería que coordinara la visita de un técnico a la escuela con carácter de urgencia."
            ),
            mistakes = listOf(
                "I want my money back now!" to "I must insist on an immediate refund. (Mantén el registro profesional y asertivo)"
            ),
            glossary = listOf(
                "Assertiveness" to "Asertividad: expresar derechos y exigencias con firmeza y respeto.",
                "Promptly" to "A la mayor brevedad posible."
            )
        )

        addTopic(
            id = "func_03",
            title = "Delivering Technical Presentations: Openings, Signposting & Q&A",
            titleSpanish = "Estructurar Presentaciones Técnicas y de Clase",
            category = "Funciones Comunicativas",
            moduleGroup = "Módulo 3: Presentaciones y Conferencias",
            explanation = "Frases de señalización (Signposting) para guiar a la audiencia: 1) Apertura y objetivo: 'Good morning everyone. Today I would like to talk about...'. 2) Estructura: 'I have divided my presentation into three main parts: first... second... finally...'. 3) Transición: 'Moving on to our next point...', 'This brings me to the issue of...'. 4) Conclusión y preguntas: 'To sum up...', 'I would be happy to answer any questions you may have.'",
            examples = listOf(
                "Today, we will examine how local area networks operate in secondary schools." to "Hoy examinaremos cómo operan las redes de área local en las escuelas secundarias.",
                "Turning our attention now to network security protocols, let us consider firewall configurations." to "Pasando ahora a los protocolos de seguridad de red, consideremos las configuraciones de cortafuegos."
            ),
            mistakes = listOf(
                "Pasar de un tema a otro sin avisar a la audiencia" to "Usa siempre frases puente como 'Let's now turn our attention to...'"
            ),
            glossary = listOf(
                "Signposting" to "Uso de frases guía que indican al oyente en qué punto de la presentación nos encontramos.",
                "Wrap up" to "Concluir o resumir los puntos principales de una exposición."
            )
        )

        val a1List = TopicSeedDataA1A2.getA1Topics(startOrder = 1)
        val a2List = TopicSeedDataA1A2.getA2Topics(startOrder = a1List.size + 1)
        val b1List = TopicSeedDataB1.getB1Topics(startOrder = a1List.size + a2List.size + 1)
        val b2List = list // B2 topics

        val combined = mutableListOf<TopicEntity>()
        combined.addAll(a1List)
        combined.addAll(a2List)
        combined.addAll(b1List)
        combined.addAll(b2List)
        return combined
    }
}
