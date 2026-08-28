package com.example.data.local.seed

import com.example.data.local.entities.TopicEntity

object TopicSeedDataB1 {
    fun getB1Topics(startOrder: Int = 1): List<TopicEntity> {
        val list = mutableListOf<TopicEntity>()
        var order = startOrder

        fun addB1Topic(
            id: String,
            title: String,
            titleSpanish: String,
            category: String,
            moduleGroup: String,
            explanation: String,
            examples: List<Pair<String, String>>,
            mistakes: List<Pair<String, String>>,
            glossary: List<Pair<String, String>>,
            estMin: Int = 18
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
                    difficulty = "B1",
                    estimatedMinutes = estMin,
                    status = "NOT_STARTED"
                )
            )
        }

        // ==========================================
        // 1. GRAMÁTICA B1
        // ==========================================
        addB1Topic(
            id = "b1_gram_01",
            title = "Present Simple vs. Present Continuous",
            titleSpanish = "Presente Simple vs. Presente Continuo",
            category = "Gramática",
            moduleGroup = "Tiempos Verbales Básicos B1",
            explanation = "El Present Simple (sujeto + verbo en infinitivo / -s para he/she/it) se utiliza para expresar hábitos, rutinas diarias, verdades universales y horarios fijos ('I work every day'). El Present Continuous (am/is/are + verbo con -ing) describe acciones que ocurren en este preciso momento, situaciones temporales o planes futuros definidos ('I am working right now'). Recuerda: los verbos de estado como like, want, know y believe no se usan normalmente en forma continua.",
            examples = listOf(
                "She works in a bank, but this week she is working from home." to "Ella trabaja en un banco, pero esta semana está trabajando desde casa.",
                "I drink coffee every morning, but today I am drinking green tea." to "Bebo café todas las mañanas, pero hoy estoy bebiendo té verde.",
                "The train leaves at 8:30 AM tomorrow." to "El tren sale a las 8:30 AM mañana (horario programado)."
            ),
            mistakes = listOf(
                "I am knowing the answer." to "I know the answer. (Los verbos de estado no usan -ing)",
                "He live in Madrid." to "He lives in Madrid. (No olvides la -s en tercera persona)"
            ),
            glossary = listOf(
                "Routine" to "Hábito o acción que se repite regularmente.",
                "Temporary state" to "Situación transitoria de duración limitada."
            )
        )

        addB1Topic(
            id = "b1_gram_02",
            title = "Past Simple vs. Past Continuous",
            titleSpanish = "Pasado Simple vs. Pasado Continuo",
            category = "Gramática",
            moduleGroup = "Tiempos Verbales Básicos B1",
            explanation = "El Past Simple (verbos regulares con -ed o formas irregulares como went, ate, saw) indica acciones completadas en un momento específico del pasado. El Past Continuous (was/were + -ing) describe una acción larga que estaba en desarrollo. Con frecuencia se combinan usando 'when' (seguido de Past Simple para la acción que interrumpe) y 'while' (seguido de Past Continuous para la acción de fondo).",
            examples = listOf(
                "I was studying English when my phone rang." to "Estaba estudiando inglés cuando sonó mi teléfono.",
                "While we were walking in the park, it started to rain." to "Mientras caminábamos en el parque, empezó a llover.",
                "Yesterday, I woke up at 7:00, ate breakfast, and went to work." to "Ayer me levanté a las 7:00, desayuné y me fui al trabajo (secuencia de acciones)."
            ),
            mistakes = listOf(
                "I was study when he arrived." to "I was studying when he arrived. (El pasado continuo requiere la terminación -ing)",
                "When I was walking home, I saw an accident." to "While I was walking home, I saw an accident. (Usa 'while' para la acción continua prolongada)"
            ),
            glossary = listOf(
                "Interruption" to "Acción puntual corta que interrumpe una actividad en curso.",
                "Background action" to "Acción de fondo que proporciona el contexto temporal."
            )
        )

        addB1Topic(
            id = "b1_gram_03",
            title = "Present Perfect with For, Since, Already, Yet & Just",
            titleSpanish = "Presente Perfecto con For, Since, Already, Yet y Just",
            category = "Gramática",
            moduleGroup = "Tiempos Verbales Básicos B1",
            explanation = "El Present Perfect (have/has + participio) conecta el pasado con el presente. Usamos 'for' con periodos de tiempo (for 3 years) y 'since' con puntos de inicio específicos (since 2020). 'Just' indica que algo acaba de ocurrir; 'already' que ocurrió antes de lo esperado (afirmaciones); y 'yet' que aún no ha sucedido (preguntas y negaciones al final de la oración).",
            examples = listOf(
                "I have lived in this city for five years." to "He vivido en esta ciudad durante cinco años.",
                "She has worked here since October." to "Ella ha trabajado aquí desde octubre.",
                "I have already finished my homework." to "Ya he terminado mi tarea.",
                "Have you seen the new movie yet? No, I haven't watched it yet." to "¿Ya has visto la nueva película? No, aún no la he visto."
            ),
            mistakes = listOf(
                "I live here since three years." to "I have lived here for three years. (Usa present perfect + for con duraciones)",
                "I didn't finish yet." to "I haven't finished yet. (Con 'yet' se requiere Present Perfect en inglés estándar)"
            ),
            glossary = listOf(
                "Duration" to "Espacio de tiempo transcurrido (for 2 weeks).",
                "Starting point" to "Punto exacto en el calendario o reloj donde inició la acción (since Monday)."
            )
        )

        addB1Topic(
            id = "b1_gram_04",
            title = "Past Simple vs. Present Perfect",
            titleSpanish = "Pasado Simple vs. Presente Perfecto",
            category = "Gramática",
            moduleGroup = "Tiempos Verbales Básicos B1",
            explanation = "Usa Past Simple cuando el tiempo está terminado o especificado con expresiones como yesterday, in 2019, last week, ago o when. Usa Present Perfect cuando el tiempo no está terminado (today, this week, in my life) o cuando la acción tiene un efecto o relevancia directa en el presente sin importar cuándo ocurrió.",
            examples = listOf(
                "I lost my keys yesterday." to "Perdí mis llaves ayer (tiempo terminado y definido).",
                "I have lost my keys; I can't open the door now." to "He perdido mis llaves; no puedo abrir la puerta ahora (efecto presente).",
                "Did you visit London last year?" to "¿Visitaste Londres el año pasado?",
                "Have you ever visited London?" to "¿Alguna vez has visitado Londres? (en tu vida)"
            ),
            mistakes = listOf(
                "I have seen him yesterday." to "I saw him yesterday. (Nunca uses Present Perfect con 'yesterday' o 'last week')",
                "I lived here for 10 years and I still live here." to "I have lived here for 10 years. (Si la acción continúa, usa Present Perfect)"
            ),
            glossary = listOf(
                "Finished time marker" to "Expresiones que cierran el periodo temporal (yesterday, last night, in 2015).",
                "Life experience" to "Experiencias de vida en un periodo abierto (ever, never)."
            )
        )

        addB1Topic(
            id = "b1_gram_05",
            title = "Future Forms: Will vs. Be Going To vs. Present Continuous",
            titleSpanish = "Formas de Futuro: Will vs. Be Going To vs. Presente Continuo",
            category = "Gramática",
            moduleGroup = "Futuro y Modales B1",
            explanation = "1) 'Will + base form': decisiones espontáneas tomadas en el momento de hablar ('I will help you'), promesas y predicciones basadas en opinión. 2) 'Be going to + base form': intenciones o planes previos ('I'm going to buy a car') y predicciones basadas en evidencia visual presente ('Look at those dark clouds! It's going to rain'). 3) 'Present Continuous': citas fijas y acuerdos con hora/lugar ('I am meeting Carlos tomorrow at 4 PM').",
            examples = listOf(
                "The phone is ringing. I will answer it!" to "El teléfono está sonando. ¡Yo contesto! (decisión espontánea)",
                "I am going to study medicine next year." to "Voy a estudiar medicina el próximo año (intención/plan).",
                "We are flying to Paris on Friday; we already bought the tickets." to "Volamos a París el viernes; ya compramos los boletos (acuerdo confirmado)."
            ),
            mistakes = listOf(
                "Look at the sky, it will rain." to "Look at the sky, it is going to rain. (Predicción con evidencia visual requiere 'be going to')",
                "I will meet the doctor at 3 PM tomorrow." to "I am meeting the doctor at 3 PM tomorrow. (Cita médica programada)"
            ),
            glossary = listOf(
                "Spontaneous decision" to "Decisión tomada al instante sin premeditación.",
                "Fixed arrangement" to "Compromiso o acuerdo confirmado con otra persona o entidad."
            )
        )

        addB1Topic(
            id = "b1_gram_06",
            title = "Modal Verbs: Can, Could, Be Able To & May",
            titleSpanish = "Modales de Habilidad y Permiso",
            category = "Gramática",
            moduleGroup = "Futuro y Modales B1",
            explanation = "'Can' expresa habilidad presente o permiso informal ('I can swim', 'Can I sit here?'). 'Could' expresa habilidad general pasada ('When I was young, I could run fast') o peticiones más educadas ('Could you help me, please?'). 'Be able to' se usa para tiempos que 'can' no tiene (futuro: will be able to, present perfect: have been able to). 'May' expresa permiso formal o posibilidad media ('May I leave early?').",
            examples = listOf(
                "She can speak three languages fluently." to "Ella puede hablar tres idiomas con fluidez.",
                "Could you open the window, please?" to "¿Podrías abrir la ventana, por favor? (petición formal y cortés)",
                "With this new app, you will be able to practice every day." to "Con esta nueva app, podrás practicar todos los días.",
                "May I ask a question?" to "¿Me permite hacer una pregunta? (permiso formal)"
            ),
            mistakes = listOf(
                "I will can travel next month." to "I will be able to travel next month. ('Can' no tiene forma de futuro con will)",
                "He can to play guitar." to "He can play guitar. (Los verbos modales van seguidos de infinitivo sin 'to')"
            ),
            glossary = listOf(
                "Polite request" to "Petición cordial y educada.",
                "General ability" to "Capacidad física o intelectual de realizar una tarea."
            )
        )

        addB1Topic(
            id = "b1_gram_07",
            title = "Modals of Obligation & Advice: Must, Have to, Should & Mustn't",
            titleSpanish = "Modales de Obligación, Prohibición y Consejo",
            category = "Gramática",
            moduleGroup = "Futuro y Modales B1",
            explanation = "'Must' y 'Have to' expresan obligación firme ('You must wear a seatbelt', 'I have to wake up early for work'). 'Mustn't' significa prohibición absoluta ('You mustn't smoke here'). 'Don't have to' significa que NO es necesario o es opcional ('You don't have to pay now'). 'Should / Shouldn't' expresa recomendaciones y consejos ('You should sleep at least 8 hours').",
            examples = listOf(
                "You must show your passport at the border." to "Debes mostrar tu pasaporte en la frontera (obligación/ley).",
                "You don't have to come tomorrow if you are busy." to "No tienes que venir mañana si estás ocupado (no es obligatorio).",
                "You mustn't park in front of the emergency exit." to "No debes estacionar frente a la salida de emergencia (prohibido).",
                "You should drink more water every day." to "Deberías beber más agua todos los días (consejo)."
            ),
            mistakes = listOf(
                "You mustn't wear a uniform on casual Friday." to "You don't have to wear a uniform on casual Friday. ('Mustn't' = prohibido; 'don't have to' = no es necesario)",
                "You should to see a doctor." to "You should see a doctor. ('Should' va directo con infinitivo sin 'to')"
            ),
            glossary = listOf(
                "Obligation" to "Deber o necesidad forzosa.",
                "Lack of obligation" to "Ausencia de obligación (libertad de elección)."
            )
        )

        addB1Topic(
            id = "b1_gram_08",
            title = "First Conditional (Real Possibilities)",
            titleSpanish = "Primer Condicional: Posibilidades Reales",
            category = "Gramática",
            moduleGroup = "Condicionales y Pasiva B1",
            explanation = "El First Conditional se utiliza para hablar sobre situaciones futuras reales, probables o posibles y sus consecuencias directas. Estructura: 'If + Present Simple, will/won't + verbo base' (o a la inversa: 'Will + verbo base + if + Present Simple'). ¡Cuidado!: nunca uses 'will' en la cláusula de 'if'.",
            examples = listOf(
                "If it rains tomorrow, we will stay at home." to "Si llueve mañana, nos quedaremos en casa.",
                "If you study hard, you will pass the B1 exam." to "Si estudias duro, aprobarás el examen B1.",
                "I will call you if I arrive early." to "Te llamaré si llego temprano."
            ),
            mistakes = listOf(
                "If it will rain, we will stay inside." to "If it rains, we will stay inside. (Nunca uses 'will' dentro de la cláusula con 'if')",
                "If she don't hurry, she will miss the bus." to "If she doesn't hurry, she will miss the bus. (Recuerda la conjugación de 3ª persona)"
            ),
            glossary = listOf(
                "Condition" to "Requisito o supuesto introducido por 'if'.",
                "Consequence" to "Resultado futuro garantizado si se cumple la condición."
            )
        )

        addB1Topic(
            id = "b1_gram_09",
            title = "Second Conditional (Hypothetical Situations)",
            titleSpanish = "Segundo Condicional: Situaciones Hipotéticas",
            category = "Gramática",
            moduleGroup = "Condicionales y Pasiva B1",
            explanation = "El Second Conditional se usa para situaciones imaginarias, hipotéticas o poco probables en el presente o futuro. Estructura: 'If + Past Simple, would + verbo base'. Para dar consejos se utiliza comúnmente la expresión fija 'If I were you, I would...'. En inglés formal se prefiere 'were' para todas las personas (If I were, If he were).",
            examples = listOf(
                "If I won the lottery, I would travel around the world." to "Si ganara la lotería, viajaría por todo el mundo.",
                "If I were you, I would accept the job offer." to "Si yo fuera tú, aceptaría la oferta de trabajo (consejo).",
                "If they had more free time, they would learn French." to "Si ellos tuvieran más tiempo libre, aprenderían francés."
            ),
            mistakes = listOf(
                "If I would have money, I would buy a house." to "If I had money, I would buy a house. (Nunca uses 'would' en la cláusula del 'if')",
                "If I was you..." to "If I were you... (En exámenes formales B1 siempre se prefiere 'were')"
            ),
            glossary = listOf(
                "Hypothetical" to "Supuesto ficticio no correspondiente a la realidad actual.",
                "Advice formula" to "Estructura estándar para aconsejar: 'If I were you...'"
            )
        )

        addB1Topic(
            id = "b1_gram_10",
            title = "Passive Voice (Present & Past Simple)",
            titleSpanish = "Voz Pasiva en Presente y Pasado Simple",
            category = "Gramática",
            moduleGroup = "Condicionales y Pasiva B1",
            explanation = "La voz pasiva se usa cuando el objeto de la acción o el proceso en sí es más importante que la persona que lo realiza, o cuando el agente es desconocido. Presente Pasivo: am/is/are + participio pasado ('English is spoken here'). Pasado Pasivo: was/were + participio pasado ('The bridge was built in 1995'). Para mencionar quién realiza la acción, usamos 'by'.",
            examples = listOf(
                "Millions of emails are sent every minute." to "Millones de correos electrónicos son enviados cada minuto.",
                "The telephone was invented by Alexander Graham Bell." to "El teléfono fue inventado por Alexander Graham Bell.",
                "These cars are manufactured in Germany." to "Estos autos son fabricados en Alemania."
            ),
            mistakes = listOf(
                "The book wrote in 1920." to "The book was written in 1920. (Requiere verbo to be + participio)",
                "English is speak all over the world." to "English is spoken all over the world. (El verbo principal debe estar en participio)"
            ),
            glossary = listOf(
                "Agent" to "Persona u objeto que ejecuta la acción, introducido por 'by'.",
                "Past participle" to "Tercera columna verbal (written, made, done, spoken)."
            )
        )

        addB1Topic(
            id = "b1_gram_11",
            title = "Defining Relative Clauses: Who, Which, That, Where & Whose",
            titleSpanish = "Oraciones de Relativo Especificativas",
            category = "Gramática",
            moduleGroup = "Estructuras Complejas B1",
            explanation = "Las oraciones de relativo especificativas proporcionan información esencial para identificar a una persona, cosa o lugar sin la cual la frase pierde sentido. Usa: 'who' o 'that' para personas ('The man who called you'), 'which' o 'that' para objetos/animales ('The book that I read'), 'where' para lugares ('The city where I was born') y 'whose' para posesión ('The student whose bag was stolen').",
            examples = listOf(
                "A doctor is a person who treats sick people." to "Un médico es una persona que atiende a personas enfermas.",
                "This is the hotel where we stayed last summer." to "Este es el hotel donde nos hospedamos el verano pasado.",
                "I bought a laptop that has 16 GB of RAM." to "Compré una laptop que tiene 16 GB de RAM.",
                "That is the woman whose dog won the contest." to "Esa es la mujer cuyo perro ganó el concurso."
            ),
            mistakes = listOf(
                "The doctor which helped me was very kind." to "The doctor who/that helped me was very kind. (Usa 'who' para seres humanos, no 'which')",
                "The town which I live..." to "The town where I live... (Para indicar locación o lugar usa 'where')"
            ),
            glossary = listOf(
                "Relative pronoun" to "Pronombre que enlaza dos cláusulas (who, which, that, where, whose).",
                "Essential info" to "Información indispensable para definir al sujeto."
            )
        )

        addB1Topic(
            id = "b1_gram_12",
            title = "Comparatives, Superlatives, As...as & Too/Enough",
            titleSpanish = "Comparativos, Superlativos, As...as y Too/Enough",
            category = "Gramática",
            moduleGroup = "Estructuras Complejas B1",
            explanation = "Comparativos: adjetivos cortos añaden '-er than' (faster than); adjetivos largos usan 'more ... than' (more expensive than). Superlativos: 'the -est' (the fastest) o 'the most ...' (the most interesting). Para igualdad: 'as + adjetivo + as' ('as tall as'). 'Too' va antes del adjetivo y significa 'demasiado' ('too hot'). 'Enough' va DESPUÉS del adjetivo y significa 'suficiente' ('warm enough'), pero ANTES de los sustantivos ('enough time').",
            examples = listOf(
                "Public transport is cheaper than taking a taxi." to "El transporte público es más barato que tomar un taxi.",
                "This is the most exciting book I have ever read." to "Este es el libro más emocionante que he leído jamás.",
                "My current phone is not as fast as yours." to "Mi teléfono actual no es tan rápido como el tuyo.",
                "This coffee is too hot to drink right now." to "Este café está demasiado caliente para tomarlo ahora.",
                "He isn't old enough to drive a car." to "Él no tiene la edad suficiente para conducir un auto."
            ),
            mistakes = listOf(
                "More cheaper" to "Cheaper (No combines 'more' con la terminación '-er')",
                "Enough warm" to "Warm enough ('enough' se coloca después del adjetivo)"
            ),
            glossary = listOf(
                "Comparative degree" to "Grado que contrasta dos elementos.",
                "Modifier position" to "Ubicación de 'too' (antes del adjetivo) y 'enough' (después del adjetivo)."
            )
        )

        addB1Topic(
            id = "b1_gram_13",
            title = "Gerunds vs. Infinitives (Verb + -ing vs. Verb + to)",
            titleSpanish = "Gerundios vs. Infinitivos",
            category = "Gramática",
            moduleGroup = "Estructuras Complejas B1",
            explanation = "Algunos verbos van seguidos de GERUNDIO (-ing): enjoy, avoid, suggest, mind, finish, practice, keep ('I enjoy reading'). Otros van seguidos de INFINITIVO con 'to': decide, hope, plan, offer, promise, want, need, refuse ('I decided to go'). Tras preposiciones siempre usamos gerundio ('interested in learning'). Algunos cambian de significado: 'stop smoking' (dejar el hábito) vs 'stop to smoke' (hacer una pausa para fumar).",
            examples = listOf(
                "She decided to apply for the scholarship." to "Ella decidió postularse para la beca (decide + to).",
                "I avoid driving during rush hour." to "Evito conducir durante la hora pico (avoid + -ing).",
                "Thank you for helping me with the project." to "Gracias por ayudarme con el proyecto (preposición + -ing).",
                "I will never forget visiting New York." to "Nunca olvidaré haber visitado Nueva York."
            ),
            mistakes = listOf(
                "I enjoy to watch series." to "I enjoy watching series. ('Enjoy' siempre requiere gerundio)",
                "I am interested in to study abroad." to "I am interested in studying abroad. (Tras preposición 'in' se usa -ing)"
            ),
            glossary = listOf(
                "Gerund" to "Forma verbal en -ing que funciona como sustantivo o complemento.",
                "Infinitive" to "Forma base precedida por la partícula 'to'."
            )
        )

        addB1Topic(
            id = "b1_gram_14",
            title = "Past Habits: Used to + Infinitive",
            titleSpanish = "Hábitos Pasados con Used to",
            category = "Gramática",
            moduleGroup = "Estructuras Complejas B1",
            explanation = "Usamos 'Used to + infinitivo' para hablar de estados o acciones habituales en el pasado que ya no ocurren en el presente. Forma afirmativa: 'I used to play tennis'. Forma negativa: 'I didn't use to play tennis' (nota que pierde la 'd'). Forma interrogativa: 'Did you use to live here?'. ¡Ojo!: no lo confundas con 'be used to' (estar acostumbrado).",
            examples = listOf(
                "I used to drink soda, but now I only drink water." to "Solía tomar refresco, pero ahora solo bebo agua.",
                "We didn't use to have internet when I was a child." to "No solíamos tener internet cuando yo era niño.",
                "Did you use to play video games when you were in school?" to "¿Solías jugar videojuegos cuando estabas en la escuela?"
            ),
            mistakes = listOf(
                "I didn't used to like vegetables." to "I didn't use to like vegetables. (En oraciones con 'didn't' se escribe 'use to' sin la 'd')",
                "I use to play soccer every weekend now." to "I usually play soccer every weekend now. ('Used to' solo se usa para el pasado; para el presente usa 'usually')"
            ),
            glossary = listOf(
                "Discontinued habit" to "Costumbre pasada que ya no se realiza en la actualidad.",
                "Auxiliary did" to "Auxiliar de pasado que absorbe el tiempo gramatical."
            )
        )

        addB1Topic(
            id = "b1_gram_15",
            title = "Question Tags (Present, Past & Modals)",
            titleSpanish = "Preguntas Coletilla (Question Tags)",
            category = "Gramática",
            moduleGroup = "Estructuras Complejas B1",
            explanation = "Las Question Tags son preguntas cortas al final de una frase para pedir confirmación. Regla básica: oración positiva -> tag negativa ('You are Spanish, aren't you?'); oración negativa -> tag positiva ('You haven't seen my keys, have you?'). Usa el auxiliar del tiempo verbal correspondiente (do/does/did, be, have, will, can).",
            examples = listOf(
                "You live in Guadalajara, don't you?" to "Vives en Guadalajara, ¿verdad?",
                "She didn't come to class yesterday, did she?" to "Ella no vino a clase ayer, ¿verdad?",
                "They have finished the test, haven't they?" to "Ellos han terminado el examen, ¿cierto?",
                "You will help me with the luggage, won't you?" to "Me ayudarás con el equipaje, ¿no?"
            ),
            mistakes = listOf(
                "You like pizza, isn't it?" to "You like pizza, don't you? (No uses 'isn't it' de forma genérica; usa el auxiliar 'don't')",
                "She is coming, isn't she?" to "Correcto: auxiliar 'is' invertido a negativo."
            ),
            glossary = listOf(
                "Tag question" to "Apéndice interrogativo para verificar información.",
                "Echo auxiliary" to "Auxiliar idéntico al tiempo gramatical de la frase principal."
            )
        )

        addB1Topic(
            id = "b1_gram_16",
            title = "Linking Words & Connectors: Although, However, Because, So & In Order To",
            titleSpanish = "Conectores y Marcadores Discursivos B1",
            category = "Gramática",
            moduleGroup = "Estructuras Complejas B1",
            explanation = "Los conectores dan fluidez y coherencia a tus textos y conversaciones. Contraste: 'Although' (a pesar de que + cláusula), 'However' (sin embargo, separado por comas). Causa y efecto: 'Because' (porque + razón), 'So' (así que + resultado). Propósito: 'In order to + infinitivo' (para / con el fin de).",
            examples = listOf(
                "Although it was raining heavily, we enjoyed the concert." to "Aunque estaba lloviendo intensamente, disfrutamos el concierto.",
                "He studied hard for weeks. However, he found the exam difficult." to "Él estudió duro por semanas. Sin embargo, encontró el examen difícil.",
                "I was feeling sick, so I stayed in bed." to "Me sentía enfermo, así que me quedé en cama.",
                "She moved to Canada in order to improve her English." to "Ella se mudó a Canadá con el fin de mejorar su inglés."
            ),
            mistakes = listOf(
                "Although it rained, but we went out." to "Although it rained, we went out. (Nunca uses 'although' y 'but' en la misma oración)",
                "Because of I was late..." to "Because I was late... (Usa 'because' antes de sujeto + verbo; 'because of' solo antes de sustantivos)"
            ),
            glossary = listOf(
                "Cohesion" to "Conexión lógica y fluida entre ideas y párrafos.",
                "Contrast connector" to "Palabra que opone dos hechos o argumentos."
            )
        )

        // ==========================================
        // 2. VOCABULARIO B1
        // ==========================================
        addB1Topic(
            id = "b1_voc_01",
            title = "Daily Routines, Habits & Lifestyle",
            titleSpanish = "Rutinas Diarias, Hábitos y Estilo de Vida",
            category = "Vocabulario",
            moduleGroup = "Léxico Esencial B1",
            explanation = "Vocabulario clave para describir el día a día: wake up, get dressed, commute to work, prepare meals, do household chores, take a break, hit the gym, unwind after work. Frecuencias: hardly ever, once in a while, regularly, every other day.",
            examples = listOf(
                "I usually commute by subway during morning rush hour." to "Normalmente me traslado en metro durante la hora pico de la mañana.",
                "I like to unwind by reading a book before going to sleep." to "Me gusta relajarme leyendo un libro antes de dormir.",
                "We do the grocery shopping every other Saturday." to "Hacemos las compras del supermercado un sábado sí y otro no."
            ),
            mistakes = listOf(
                "I make gym every day." to "I go to the gym / I work out every day. (Usa 'go to the gym' o 'work out')"
            ),
            glossary = listOf(
                "Commute" to "Viajar diariamente de la casa al trabajo/escuela.",
                "Unwind" to "Desconectarse, relajarse tras una jornada pesada."
            )
        )

        addB1Topic(
            id = "b1_voc_02",
            title = "Travel, Transport, Holidays & Directions",
            titleSpanish = "Viajes, Transporte, Vacaciones y Direcciones",
            category = "Vocabulario",
            moduleGroup = "Léxico Esencial B1",
            explanation = "Términos indispensables para viajar: boarding pass, luggage / baggage allowance, delayed flight, book a room, sightsee, round-trip ticket, platform, fare, take off, land. Para orientarte: turn left, go straight on, roundabout, traffic lights, cross the street.",
            examples = listOf(
                "Please have your passport and boarding pass ready at gate 12." to "Por favor tenga su pasaporte y pase de abordar listos en la puerta 12.",
                "The flight was delayed due to bad weather conditions." to "El vuelo fue retrasado debido a malas condiciones meteorológicas.",
                "Go straight on for two blocks, then turn right at the traffic lights." to "Siga derecho por dos cuadras, luego gire a la derecha en el semáforo."
            ),
            mistakes = listOf(
                "I bought two luggages." to "I bought two pieces of luggage. ('Luggage' es incontable en inglés)"
            ),
            glossary = listOf(
                "Boarding pass" to "Pase o tarjeta de embarque.",
                "Round-trip ticket" to "Boleto de ida y vuelta."
            )
        )

        addB1Topic(
            id = "b1_voc_03",
            title = "Work, Jobs, Workplace & Professional Tasks",
            titleSpanish = "Empleo, Profesiones, Oficina y Tareas Laborales",
            category = "Vocabulario",
            moduleGroup = "Léxico Esencial B1",
            explanation = "Vocabulario laboral: full-time / part-time job, salary, colleague / coworker, apply for a vacancy, schedule a meeting, meet deadlines, boss / supervisor, overtime, duties and responsibilities, shift work.",
            examples = listOf(
                "She works full-time as a software quality analyst." to "Ella trabaja de tiempo completo como analista de calidad de software.",
                "We need to schedule a meeting with the client this afternoon." to "Necesitamos agendar una reunión con el cliente esta tarde.",
                "The team worked overtime to meet the project deadline." to "El equipo trabajó horas extras para cumplir la fecha límite del proyecto."
            ),
            mistakes = listOf(
                "I have a good work." to "I have a good job. ('Job' es el puesto contable; 'work' es la actividad incontable)"
            ),
            glossary = listOf(
                "Deadline" to "Fecha u hora límite de entrega.",
                "Colleague" to "Compañero o colega de trabajo."
            )
        )

        addB1Topic(
            id = "b1_voc_04",
            title = "Shopping, Money, Prices & Consumer Services",
            titleSpanish = "Compras, Dinero, Precios y Servicios al Consumidor",
            category = "Vocabulario",
            moduleGroup = "Léxico Esencial B1",
            explanation = "Vocabulario comercial: afford, bargain, discount, receipt, refund, exchange, cash or credit card, fitting room, customer service, pay in instalments, wallet, banknote / coin.",
            examples = listOf(
                "I got a 20% discount on these running shoes." to "Obtuve un descuento del 20% en estos tenis para correr.",
                "Keep your receipt in case you want to request a refund." to "Conserva tu recibo por si quieres solicitar un reembolso.",
                "I cannot afford to buy a new laptop right now." to "No puedo permitirme económicamente comprar una laptop nueva ahora."
            ),
            mistakes = listOf(
                "Can you give me the ticket?" to "Can you give me the receipt? ('Receipt' es el comprobante de compra; 'ticket' es el boleto de viaje o evento)"
            ),
            glossary = listOf(
                "Afford" to "Tener solvencia económica para pagar algo.",
                "Refund" to "Devolución de dinero por un producto devuelto."
            )
        )

        addB1Topic(
            id = "b1_voc_05",
            title = "Health, Illnesses, Symptoms & Medical Advice",
            titleSpanish = "Salud, Enfermedades Comunes, Síntomas y Consejos Médicos",
            category = "Vocabulario",
            moduleGroup = "Léxico Esencial B1",
            explanation = "Términos de salud: headache, sore throat, cough, fever, painkiller, prescription, appointment, recover, symptoms, feel dizzy, bandage, stay in bed, emergency room.",
            examples = listOf(
                "I have a terrible sore throat and a mild fever." to "Tengo un dolor de garganta terrible y una fiebre leve.",
                "The doctor gave me a prescription for antibiotics." to "El médico me dio una receta para antibióticos.",
                "Take this painkiller every eight hours after meals." to "Toma este analgésico cada ocho horas después de las comidas."
            ),
            mistakes = listOf(
                "I have flu." to "I have the flu. (Se dice 'have the flu' con artículo)",
                "I am constipated (significa estreñido, no resfriado)." to "I have a cold. (Para resfriado común usa 'have a cold')"
            ),
            glossary = listOf(
                "Prescription" to "Receta médica escrita por un doctor.",
                "Painkiller" to "Analgésico o medicamento para calmar el dolor."
            )
        )

        addB1Topic(
            id = "b1_voc_06",
            title = "Food, Cooking, Restaurants & Ordering Meals",
            titleSpanish = "Alimentación, Cocina, Restaurantes y Pedir Comida",
            category = "Vocabulario",
            moduleGroup = "Léxico Esencial B1",
            explanation = "Vocabulario gastronómico: appetizer / starter, main course, dessert, bill / check, tip, vegetarian / vegan, spicy, delicious, recipe, ingredients, bake, fry, boil, slice, grilled.",
            examples = listOf(
                "Could we have the bill, please? We would like to leave a tip." to "¿Nos trae la cuenta, por favor? Nos gustaría dejar propina.",
                "For the main course, I would like grilled salmon with vegetables." to "Para el plato fuerte, me gustaría salmón a la parrilla con verduras.",
                "Is this dish suitable for vegetarians?" to "¿Este platillo es apto para vegetarianos?"
            ),
            mistakes = listOf(
                "Bring me the account." to "Could I have the bill/check, please? ('Account' es cuenta bancaria o de usuario; la cuenta del restaurante es 'bill' o 'check')"
            ),
            glossary = listOf(
                "Main course" to "Plato fuerte o principal de una comida.",
                "Tip" to "Propina para el servicio del mesero."
            )
        )

        addB1Topic(
            id = "b1_voc_07",
            title = "House, Accommodation, Furniture & Chores",
            titleSpanish = "Vivienda, Alojamientos, Muebles y Tareas Domésticas",
            category = "Vocabulario",
            moduleGroup = "Léxico Esencial B1",
            explanation = "Partes de la casa y enseres: living room, kitchen, bedroom, balcony, attic, rent / tenant, landlord, vacuum the carpet, wash the dishes, take out the trash, tidy up, iron clothes, central heating.",
            examples = listOf(
                "The apartment has two bedrooms and a spacious living room." to "El departamento tiene dos recámaras y una sala espaciosa.",
                "We share the household chores: I wash the dishes and he vacuums." to "Nos dividimos las tareas del hogar: yo lavo los platos y él pasa la aspiradora.",
                "The landlord promised to repair the water heater tomorrow." to "El casero prometió reparar el calentador de agua mañana."
            ),
            mistakes = listOf(
                "I made the dishes." to "I did the dishes / I washed the dishes. (La colocación fija es 'do the dishes')"
            ),
            glossary = listOf(
                "Household chore" to "Tarea doméstica rutinaria de limpieza y orden.",
                "Landlord" to "Arrendador o propietario del inmueble."
            )
        )

        addB1Topic(
            id = "b1_voc_08",
            title = "Technology, Mobile Devices, Internet & Social Media",
            titleSpanish = "Tecnología, Dispositivos Móviles, Internet y Redes Sociales",
            category = "Vocabulario",
            moduleGroup = "Léxico Esencial B1",
            explanation = "Tecnología actual: download / upload, charge the battery, password, Wi-Fi network, attach a file, app, screen, log in / log out, notifications, browse the web, backup files, cybersecurity.",
            examples = listOf(
                "Don't forget to attach your resume to the email." to "No olvides adjuntar tu currículum al correo electrónico.",
                "My phone battery is low; can I borrow your charger?" to "La batería de mi teléfono está baja; ¿me prestas tu cargador?",
                "Make sure you create a strong password for your account." to "Asegúrate de crear una contraseña segura para tu cuenta."
            ),
            mistakes = listOf(
                "I will charge my balance." to "I will top up my credit / recharge my account. ('Charge' se usa para la batería; para saldo telefónico se usa 'top up')"
            ),
            glossary = listOf(
                "Attach" to "Adjuntar un archivo a un mensaje digital.",
                "Log in" to "Iniciar sesión ingresando credenciales."
            )
        )

        addB1Topic(
            id = "b1_voc_09",
            title = "Weather, Environment & Nature",
            titleSpanish = "Clima, Estaciones del Año y Medio Ambiente",
            category = "Vocabulario",
            moduleGroup = "Léxico Esencial B1",
            explanation = "Términos climáticos y ecológicos: sunny, cloudy, windy, freezing, mild, heatwave, storm, recycle, pollution, renewable energy, climate change, forest, wildlife, protect the planet.",
            examples = listOf(
                "The weather forecast says it will be sunny and warm this weekend." to "El pronóstico del tiempo dice que estará soleado y templado este fin de semana.",
                "We must reduce plastic waste to protect ocean wildlife." to "Debemos reducir los residuos de plástico para proteger la fauna marina.",
                "It was freezing cold yesterday morning." to "Hacía un frío helador ayer por la mañana."
            ),
            mistakes = listOf(
                "It is making cold." to "It is cold. (En inglés el clima usa el verbo 'to be', no 'make')"
            ),
            glossary = listOf(
                "Forecast" to "Pronóstico o predicción meteorológica.",
                "Renewable energy" to "Energía renovable y sostenible (solar, eólica)."
            )
        )

        addB1Topic(
            id = "b1_voc_10",
            title = "Describing People: Personality, Feelings & Appearance",
            titleSpanish = "Descripción de Personas: Personalidad, Sentimientos y Apariencia",
            category = "Vocabulario",
            moduleGroup = "Léxico Esencial B1",
            explanation = "Personalidad: cheerful, stubborn, reliable, outgoing, shy, patient, generous, ambitious. Sentimientos: anxious, excited, proud, disappointed, relieved. Apariencia: tall, average height, curly hair, beard, casual clothes.",
            examples = listOf(
                "Ana is very reliable; you can always count on her." to "Ana es muy confiable; siempre puedes contar con ella.",
                "He felt relieved when he heard the good news." to "Él se sintió aliviado cuando escuchó las buenas noticias.",
                "My brother has short curly hair and a friendly smile." to "Mi hermano tiene cabello corto y rizado y una sonrisa amigable."
            ),
            mistakes = listOf(
                "She is very sensible (significa sensata/prudente)." to "She is very sensitive. (Para 'sensible emocionalmente' usa 'sensitive')"
            ),
            glossary = listOf(
                "Reliable" to "Persona confiable y cumplidora.",
                "Relieved" to "Aliviado, libre de angustia."
            )
        )

        addB1Topic(
            id = "b1_voc_11",
            title = "Essential B1 Phrasal Verbs",
            titleSpanish = "Phrasal Verbs Fundamentales de Nivel B1",
            category = "Vocabulario",
            moduleGroup = "Léxico Esencial B1",
            explanation = "Phrasal verbs imprescindibles: look for (buscar), look after (cuidar), give up (rendirse/dejar un hábito), turn on/off (encender/apagar), find out (averiguar/descubrir), put on (ponerse ropa), take off (quitarse ropa / despegar avión), get on well with (llevarse bien con), run out of (quedarse sin algo).",
            examples = listOf(
                "I am looking for my keys; have you seen them?" to "Estoy buscando mis llaves; ¿las has visto?",
                "He decided to give up smoking for his health." to "Él decidió dejar de fumar por su salud.",
                "We ran out of milk, so I need to go to the store." to "Nos quedamos sin leche, así que necesito ir a la tienda.",
                "I get on very well with my new coworkers." to "Me llevo muy bien con mis nuevos compañeros de trabajo."
            ),
            mistakes = listOf(
                "I am looking my keys." to "I am looking for my keys. (Requiere la preposición 'for' para significar buscar)"
            ),
            glossary = listOf(
                "Phrasal verb" to "Verbo compuesto por un verbo base más una partícula que modifica su significado.",
                "Run out of" to "Agotar las existencias de un recurso o producto."
            )
        )

        addB1Topic(
            id = "b1_voc_12",
            title = "Collocations with Do, Make, Have & Take",
            titleSpanish = "Colocaciones Clave con Do, Make, Have y Take",
            category = "Vocabulario",
            moduleGroup = "Léxico Esencial B1",
            explanation = "Colocaciones fijas: con DO: do homework, do business, do a favor, do your best. Con MAKE: make a mistake, make a decision, make dinner, make noise, make an appointment. Con HAVE: have lunch, have fun, have a shower, have a break. Con TAKE: take a photo, take a seat, take time, take notes, take an exam.",
            examples = listOf(
                "Don't be afraid to make mistakes; that's how you learn." to "No tengas miedo de cometer errores; así es como aprendes.",
                "Please take a seat while you wait for the manager." to "Por favor tome asiento mientras espera al gerente.",
                "I need to do my best in tomorrow's interview." to "Necesito dar mi mejor esfuerzo en la entrevista de mañana."
            ),
            mistakes = listOf(
                "I made my homework." to "I did my homework. (Las tareas académicas siempre usan 'do')",
                "I took a photo." to "Correcto: la acción de fotografiar usa 'take'."
            ),
            glossary = listOf(
                "Collocation" to "Combinación natural y habitual de dos o más palabras en inglés.",
                "Fixed expression" to "Frase consagrada por el uso nativo que no admite sustituciones libres."
            )
        )

        // ==========================================
        // 3. LISTENING B1
        // ==========================================
        addB1Topic(
            id = "b1_list_01",
            title = "Public Announcements (Airports, Stations & Stores)",
            titleSpanish = "Anuncios Públicos en Aeropuertos y Estaciones",
            category = "Listening",
            moduleGroup = "Comprensión Auditiva B1",
            explanation = "Estrategias B1: En los avisos por megafonía identifica las palabras clave de destino, número de tren/vuelo, puerta (gate), andén (platform), retrasos (delay) o cancelaciones. No intentes traducir cada palabra; enfócate en el mensaje operativo y los números.",
            examples = listOf(
                "Attention all passengers for flight BA 249 to Madrid: please proceed to gate 14 immediately." to "Atención pasajeros del vuelo BA 249 a Madrid: favor de presentarse en la puerta 14 de inmediato.",
                "The next train arriving at platform 3 is the 10:15 service to Oxford." to "El siguiente tren que llega al andén 3 es el servicio de las 10:15 con destino a Oxford."
            ),
            mistakes = listOf(
                "Concentrarse en entender palabras secundarias en lugar de captar la puerta, la hora y el destino." to "Escucha selectiva: anota solo los datos numéricos y de acción."
            ),
            glossary = listOf(
                "Public Address (PA)" to "Sistema de sonido y megafonía pública.",
                "Platform / Gate" to "Andén de tren / Puerta de embarque aéreo."
            )
        )

        addB1Topic(
            id = "b1_list_02",
            title = "Numbers, Times, Dates, Prices & Spelling",
            titleSpanish = "Identificación de Números, Horas, Precios y Deletreo",
            category = "Listening",
            moduleGroup = "Comprensión Auditiva B1",
            explanation = "En exámenes Cambridge PET (B1 Listening Part 2/3), el deletreo de apellidos o direcciones y las cifras son evaluadas. Presta atención a la diferencia fonética entre los números en -teen (thirteen /θɜːˈtiːn/) con acento al final, y en -ty (thirty /ˈθɜːti/) con acento al inicio.",
            examples = listOf(
                "My last name is spelled W-I-L-S-O-N." to "Mi apellido se deletrea W-I-L-S-O-N.",
                "The museum ticket costs fourteen dollars for adults and eight for children." to "El boleto del museo cuesta catorce dólares para adultos y ocho para niños."
            ),
            mistakes = listOf(
                "Confundir 15 (fifteen) con 50 (fifty)." to "Fíjate en el estrés silábico: fifTEEN suena largo al final; FIFty es corto y átono."
            ),
            glossary = listOf(
                "Spelling" to "Deletreo fonético letra por letra de nombres propios.",
                "Syllable stress" to "Golpe de voz que distingue cifras similares."
            )
        )

        addB1Topic(
            id = "b1_list_03",
            title = "Daily Conversations & Detecting Agreement/Disagreement",
            titleSpanish = "Diálogos Cotidianos: Detección de Opiniones y Acuerdos",
            category = "Listening",
            moduleGroup = "Comprensión Auditiva B1",
            explanation = "En diálogos entre dos personas, los hablantes suelen negociar planes. Escucha marcadores de acuerdo ('I totally agree', 'Sounds like a great idea') y de desacuerdo o duda ('I'm not so sure about that', 'Actually, I would prefer...'). El acuerdo final suele llegar hacia el final de la conversación.",
            examples = listOf(
                "Shall we go to the cinema tonight? Well, I'd rather stay home and watch a documentary." to "¿Vamos al cine hoy? Bueno, preferiría quedarme en casa y ver un documental.",
                "I think the blue shirt looks nicer on you. Yes, you're right, I'll take it." to "Creo que la camisa azul se te ve mejor. Sí, tienes razón, me la llevo."
            ),
            mistakes = listOf(
                "Elegir la primera opción que mencionan antes de escuchar si ambos interlocutores están de acuerdo." to "Espera al desenlace del diálogo para confirmar el acuerdo mutuo."
            ),
            glossary = listOf(
                "Distractor" to "Opción mencionada inicialmente pero luego descartada por los hablantes.",
                "Consensus" to "Acuerdo alcanzado tras debatir dos alternativas."
            )
        )

        // ==========================================
        // 4. SPEAKING B1
        // ==========================================
        addB1Topic(
            id = "b1_spk_01",
            title = "Describing Photographs (Technique for Cambridge B1)",
            titleSpanish = "Técnica de Descripción de Fotografías",
            category = "Speaking",
            moduleGroup = "Expresión Oral B1",
            explanation = "Estructura en 4 pasos para hablar 1 minuto sobre una foto: 1) Visión general ('In this picture I can see two young people...'). 2) Lugar y ambiente ('They seem to be in a modern kitchen, cooking dinner'). 3) Acciones y ropa ('The woman is chopping vegetables while the man is smiling'). 4) Especulación ('It looks like they are preparing food for a party because they look happy').",
            examples = listOf(
                "In the foreground, there is a man sitting at a desk with a laptop." to "En primer plano, hay un hombre sentado en un escritorio con una laptop.",
                "In the background, I can see some books on the shelf." to "Al fondo, puedo ver algunos libros en el estante.",
                "They are both wearing casual clothes, so it might be the weekend." to "Ambos llevan ropa casual, así que podría ser fin de semana."
            ),
            mistakes = listOf(
                "Decir solo listas de objetos sueltos ('chair, table, girl')." to "Construye oraciones completas en presente continuo ('A girl is reading near the window')."
            ),
            glossary = listOf(
                "Foreground" to "Primer plano visual de la imagen.",
                "Background" to "Fondo o parte posterior del escenario."
            )
        )

        addB1Topic(
            id = "b1_spk_02",
            title = "Collaborative Discussion & Making Suggestions",
            titleSpanish = "Discusión en Pareja y Toma de Decisiones",
            category = "Speaking",
            moduleGroup = "Expresión Oral B1",
            explanation = "En la interacción oral B1, debes proponer ideas y hacer que tu compañero participe. Fórmulas de propuesta: 'Why don't we...?', 'How about + -ing...?', 'Shall we...?'. Fórmulas para pedir opinión: 'What do you think?', 'Do you agree with that?'. Para consensuar: 'So, let's choose this one'.",
            examples = listOf(
                "Why don't we organize a surprise birthday dinner?" to "¿Por qué no organizamos una cena sorpresa de cumpleaños?",
                "How about giving her a gift card instead of clothes?" to "¿Qué tal si le damos una tarjeta de regalo en lugar de ropa?",
                "That's a very good point, but what do you think about going to the beach?" to "Es un muy buen punto, pero ¿qué opinas de ir a la playa?"
            ),
            mistakes = listOf(
                "Monopolizar la conversación sin dar oportunidad al compañero de responder." to "Haz preguntas de seguimiento ('What's your view on this?') tras dar tu opinión."
            ),
            glossary = listOf(
                "Turn-taking" to "Alternancia respetuosa de turnos al hablar.",
                "Suggestion phrase" to "Estructura para proponer una alternativa amablemente."
            )
        )

        addB1Topic(
            id = "b1_spk_03",
            title = "Expressing Preferences & Giving Reasons",
            titleSpanish = "Expresar Preferencias y Dar Razones",
            category = "Speaking",
            moduleGroup = "Expresión Oral B1",
            explanation = "Para alcanzar nivel B1 debes justificar tus gustos. Estructuras clave: 'I prefer [noun/-ing] to [noun/-ing]' ('I prefer cycling to driving'), 'I would rather + base form' ('I would rather cook at home'), 'The main reason is that...', 'For instance / For example...'.",
            examples = listOf(
                "I prefer studying in the library because it is much quieter than my room." to "Prefiero estudiar en la biblioteca porque es mucho más silenciosa que mi recámara.",
                "I would rather travel by train than by plane because you can enjoy the scenery." to "Preferiría viajar en tren que en avión porque puedes disfrutar del paisaje."
            ),
            mistakes = listOf(
                "I prefer coffee than tea." to "I prefer coffee to tea. (La preposición fija con 'prefer' es 'to', jamás 'than')",
                "I'd rather to stay." to "I'd rather stay. ('Would rather' va sin 'to')"
            ),
            glossary = listOf(
                "Preference" to "Elección favorable entre dos o más opciones.",
                "Justification" to "Argumento que explica el motivo de tu preferencia."
            )
        )

        // ==========================================
        // 5. READING B1
        // ==========================================
        addB1Topic(
            id = "b1_read_01",
            title = "Public Notices, Short Messages & Signs",
            titleSpanish = "Avisos Públicos, Señales y Mensajes Cortos",
            category = "Reading",
            moduleGroup = "Comprensión Lectora B1",
            explanation = "En la Parte 1 del Reading B1, analizas avisos reales (letreros de advertencia, notas de compañeros, correos breves). Identifica el propósito principal: ¿es una advertencia, una invitación, una instrucción o una disculpa? Busca paráfrasis y sinónimos en las opciones de respuesta.",
            examples = listOf(
                "Notice: 'Library books must be returned within 14 days or a fine applies.'" to "Significado: Los usuarios deben devolver los libros a tiempo para evitar pagar una multa.",
                "Email: 'Can you pick up bread on your way home? Thanks!'" to "Significado: Un familiar le pide a otro que compre pan."
            ),
            mistakes = listOf(
                "Elegir la respuesta solo porque repite la misma palabra exacta del letrero." to "Las respuestas correctas suelen ser paráfrasis con palabras distintas pero idéntico significado."
            ),
            glossary = listOf(
                "Paraphrase" to "Reescribir una idea con sinónimos manteniendo el sentido.",
                "Fine" to "Multa o recargo económico por incumplimiento."
            )
        )

        addB1Topic(
            id = "b1_read_02",
            title = "Scanning for Specific Information in Menus & Schedules",
            titleSpanish = "Búsqueda Rápida de Información (Scanning)",
            category = "Reading",
            moduleGroup = "Comprensión Lectora B1",
            explanation = "El 'Scanning' consiste en mover los ojos rápidamente por el texto buscando un dato concreto (precios, horarios, nombres, requerimientos de edad) sin leer cada línea en detalle. Usa palabras clave de la pregunta para ubicar la sección exacta del folleto o tabla.",
            examples = listOf(
                "Búsqueda de horario: 'Departures from Gate 4 start at 09:30 AM.'" to "Identificación directa de la hora sin distraerse con las amenidades del aeropuerto.",
                "Folleto de actividades: 'Free admission for children under 12 on Sundays.'" to "Ubicación de condiciones de gratuidad y edades límite."
            ),
            mistakes = listOf(
                "Leer todo el folleto desde el inicio perdiendo valiosos minutos del examen." to "Ve directo a la pregunta, extrae la palabra clave y haz barrido visual."
            ),
            glossary = listOf(
                "Scanning" to "Lectura rápida para localizar datos puntuales.",
                "Admission fee" to "Costo de entrada a un recinto o evento."
            )
        )

        addB1Topic(
            id = "b1_read_03",
            title = "Reading for Gist & Contextual Vocabulary",
            titleSpanish = "Comprensión Global e Inferencia de Vocabulario",
            category = "Reading",
            moduleGroup = "Comprensión Lectora B1",
            explanation = "Lectura global ('Skimming'): entender la idea central de un artículo sobre viajes, ciencia o pasatiempos. Cuando encuentres una palabra desconocida, deduce su significado observando la categoría gramatical (¿es verbo, adjetivo, sustantivo?) y las pistas del contexto anterior y posterior.",
            examples = listOf(
                "The ancient castle was 'breathtaking', and all tourists stopped to take photos." to "'Breathtaking' es positivo e impresionante por la reacción de los turistas.",
                "The bridge was temporarily 'closed' due to heavy snow." to "'Closed' indica que el tránsito no estaba permitido por la nevada."
            ),
            mistakes = listOf(
                "Detener la lectura cada vez que aparece una palabra que no conoces." to "Continúa leyendo para entender el mensaje general por contexto."
            ),
            glossary = listOf(
                "Gist" to "Idea principal o esencia de un texto.",
                "Context clues" to "Pistas en las palabras vecinas que revelan el significado."
            )
        )

        // ==========================================
        // 6. WRITING B1
        // ==========================================
        addB1Topic(
            id = "b1_wri_01",
            title = "Informal Email Writing (80-100 words)",
            titleSpanish = "Redacción de Email Informal",
            category = "Writing",
            moduleGroup = "Expresión Escrita B1",
            explanation = "Estructura B1: 1) Saludo amistoso ('Hi Sarah,', 'Dear Mark,'). 2) Apertura agradable ('Thanks for your email. It was great to hear from you!'). 3) Cuerpo respondiendo a las 4 preguntas de la instrucción (usa conectores como also, because, so). 4) Cierre cordial ('Write back soon,', 'All the best,') y tu nombre de firma.",
            examples = listOf(
                "Hi Alex,\nThanks for inviting me to your party! I'd love to come. I can bring some snacks and a playlist. Let me know what time it starts.\nSee you soon,\nCarlos" to "Ejemplo de email informal conciso y bien estructurado."
            ),
            mistakes = listOf(
                "Usar fórmulas demasiado formales como 'Yours faithfully' o 'Dear Sir/Madam' en un email a un amigo." to "Usa lenguaje cotidiano, contracciones (I'd, can't) y saludos cercanos."
            ),
            glossary = listOf(
                "Informal register" to "Tono relajado y natural apropiado para amigos o compañeros.",
                "Sign-off" to "Fórmula de despedida antes de tu nombre."
            )
        )

        addB1Topic(
            id = "b1_wri_02",
            title = "Story Writing: Narrative Tenses & Connectors",
            titleSpanish = "Redacción de Historias: Tiempos Narrativos y Secuencia",
            category = "Writing",
            moduleGroup = "Expresión Escrita B1",
            explanation = "Para escribir una historia en B1: Comienza con la frase dada en el examen. Usa Past Simple para la secuencia de eventos (woke up, went), Past Continuous para el ambiente (the sun was shining, birds were singing) y conectores temporales de enlace: First, Then, After that, Suddenly, Fortunately, In the end.",
            examples = listOf(
                "Suddenly, the lights went out and everyone went silent." to "De repente, las luces se apagaron y todos guardaron silencio.",
                "Fortunately, we found a taxi just before it started to rain." to "Afortunadamente, encontramos un taxi justo antes de que empezara a llover."
            ),
            mistakes = listOf(
                "Escribir toda la historia en Presente Simple." to "Una narración requiere tiempos pasados (Past Simple y Continuous)."
            ),
            glossary = listOf(
                "Narrative connector" to "Palabra de secuencia cronológica (Suddenly, Meanwhile, Finally).",
                "Climax" to "Momento cumbre o giro inesperado en la historia."
            )
        )

        // ==========================================
        // 7. PRONUNCIACIÓN B1
        // ==========================================
        addB1Topic(
            id = "b1_pron_01",
            title = "Regular Past -ed Endings (/t/, /d/, /ɪd/)",
            titleSpanish = "Pronunciación de las Terminaciones -ed en Pasado",
            category = "Pronunciación",
            moduleGroup = "Fonética y Acento B1",
            explanation = "Solo se pronuncia como sílaba extra /ɪd/ cuando el verbo termina en sonido 't' o 'd' (wanted, needed, decided). Si termina en consonante sorda (p, k, s, sh, ch, f), se pronuncia como /t/ (walked /wɔːkt/, watched /wɒtʃt/). En todos los demás sonidos vocálicos y consonantes sonoras, se pronuncia como /d/ suave (played /pleɪd/, cleaned /kliːnd/).",
            examples = listOf(
                "wanted /ˈwɒntɪd/, decided /dɪˈsaɪdɪd/" to "Sonido /ɪd/ (sílaba extra)",
                "looked /lʊkt/, stopped /stɒpt/, kissed /kɪst/" to "Sonido /t/ seco sin sílaba extra",
                "played /pleɪd/, lived /lɪvd/, opened /ˈəʊpənd/" to "Sonido /d/ suave"
            ),
            mistakes = listOf(
                "Pronunciar 'walked' como 'guóked' con dos sílabas." to "Pronúncialo como una sola sílaba: /wɔːkt/."
            ),
            glossary = listOf(
                "Voiced sound" to "Sonido donde vibran las cuerdas vocales (/d/, /v/, /z/).",
                "Voiceless sound" to "Sonido producido solo con aire sin vibración (/t/, /p/, /k/, /s/)."
            )
        )

        addB1Topic(
            id = "b1_pron_02",
            title = "Short vs. Long Vowels (/ɪ/ vs. /iː/, /ʊ/ vs. /uː/)",
            titleSpanish = "Vocales Cortas vs. Vocales Largas",
            category = "Pronunciación",
            moduleGroup = "Fonética y Acento B1",
            explanation = "En inglés el significado cambia según la duración y tensión de la vocal. El par /ɪ/ (corto y relajado, como en 'ship' /ʃɪp/, 'live' /lɪv/) contrasta con /iː/ (largo y tenso con sonrisa, como en 'sheep' /ʃiːp/, 'leave' /liːv/).",
            examples = listOf(
                "ship /ʃɪp/ (barco) vs. sheep /ʃiːp/ (oveja)" to "Contraste fundamental /ɪ/ y /iː/",
                "hit /hɪt/ (golpear) vs. heat /hiːt/ (calor)" to "Contraste vocal corta vs. larga",
                "pull /pʊl/ (jalar) vs. pool /puːl/ (alberca)" to "Contraste /ʊ/ y /uː/"
            ),
            mistakes = listOf(
                "Pronunciar 'bitch' en lugar de 'beach'." to "Alarga y sonríe para decir 'beach' /biːtʃ/."
            ),
            glossary = listOf(
                "Minimal pair" to "Dos palabras que difieren en un único sonido fonético.",
                "Vowel length" to "Duración del sonido vocálico."
            )
        )

        addB1Topic(
            id = "b1_pron_03",
            title = "The Schwa Sound /ə/ & Weak Forms",
            titleSpanish = "El Sonido Schwa /ə/ y Formas Débiles",
            category = "Pronunciación",
            moduleGroup = "Fonética y Acento B1",
            explanation = "El sonido 'Schwa' /ə/ es el más común del idioma inglés. Es una vocal neutra y relajada que aparece en sílabas que NO llevan el acento principal, como la primera 'a' en 'about' /əˈbaʊt/, o en palabras funcionales como 'to', 'for', 'a', 'the', 'of'.",
            examples = listOf(
                "banana /bəˈnɑːnə/" to "La primera y última 'a' se pronuncian como Schwa /ə/.",
                "a cup of tea /ə kʌp əv tiː/" to "Tanto 'a' como 'of' usan la forma débil /ə/."
            ),
            mistakes = listOf(
                "Pronunciar todas las letras vocales con fuerza como en español." to "Relaja la boca y reduce las sílabas no acentuadas a /ə/."
            ),
            glossary = listOf(
                "Schwa" to "Símbolo fonético /ə/, la vocal más neutra y floja del inglés.",
                "Weak form" to "Pronunciación reducida de preposiciones y artículos en habla continua."
            )
        )

        // ==========================================
        // 8. FUNCIONES COMUNICATIVAS B1
        // ==========================================
        addB1Topic(
            id = "b1_func_01",
            title = "Asking for & Giving Directions in a City",
            titleSpanish = "Pedir y Dar Direcciones en una Ciudad",
            category = "Funciones Comunicativas",
            moduleGroup = "Situaciones Reales B1",
            explanation = "Preguntar: 'Excuse me, could you tell me how to get to the train station?', 'Is there a pharmacy nearby?'. Indicar: 'Go straight ahead', 'Take the second left', 'It's on your right, opposite the bank', 'Go past the supermarket'.",
            examples = listOf(
                "Excuse me, is this the right way to the museum?" to "Disculpe, ¿este es el camino correcto hacia el museo?",
                "Walk along this street for five minutes, then turn right at the traffic lights." to "Camine por esta calle durante cinco minutos, luego gire a la derecha en el semáforo."
            ),
            mistakes = listOf(
                "Say me where is the station." to "Could you tell me where the station is? (Fórmula educada con inversión indirecta)"
            ),
            glossary = listOf(
                "Opposite" to "Justo enfrente (al otro lado de la calle).",
                "Roundabout" to "Glorieta o rotonda de tránsito."
            )
        )

        addB1Topic(
            id = "b1_func_02",
            title = "Making Reservations & Checking in at Hotels/Flights",
            titleSpanish = "Reservas y Registro en Hoteles y Vuelos",
            category = "Funciones Comunicativas",
            moduleGroup = "Situaciones Reales B1",
            explanation = "Frases indispensables: 'I'd like to book a double room for two nights', 'Is breakfast included?', 'I have a reservation under the name of Martinez', 'What time is check-out?', 'Could we have a wake-up call at 7 AM?'.",
            examples = listOf(
                "I would like to check in, please. Here is my passport and confirmation number." to "Me gustaría hacer mi registro, por favor. Aquí tiene mi pasaporte y número de confirmación.",
                "Could you recommend a good local restaurant nearby?" to "¿Podría recomendar un buen restaurante local cerca de aquí?"
            ),
            mistakes = listOf(
                "I want a room." to "I would like to book a room, please. ('Would like' es mucho más cortés que 'I want')"
            ),
            glossary = listOf(
                "Check-in / Check-out" to "Hora de entrada y salida del hotel.",
                "Confirmation number" to "Código alfanumérico que acredita tu reserva."
            )
        )

        addB1Topic(
            id = "b1_func_03",
            title = "Polite Complaints, Inquiries & Returning Items in Stores",
            titleSpanish = "Reclamaciones Amables, Devoluciones y Consultas en Tiendas",
            category = "Funciones Comunicativas",
            moduleGroup = "Situaciones Reales B1",
            explanation = "En el mundo angloparlante, las quejas se expresan con suavidad diplomática: 'I'm afraid there is a small problem with...', 'I bought this shirt yesterday, but the zipper is broken', 'Could I exchange it for a larger size?', 'Would it be possible to get a refund?'.",
            examples = listOf(
                "Excuse me, I'm afraid this soup is cold. Could you heat it up, please?" to "Disculpe, me temo que esta sopa está fría. ¿Podría calentarla, por favor?",
                "I would like to return this headset; it does not turn on." to "Me gustaría devolver estos audífonos; no encienden."
            ),
            mistakes = listOf(
                "This is terrible, change it now!" to "I'm afraid there is an issue with this item; could you please help me exchange it?"
            ),
            glossary = listOf(
                "I'm afraid that..." to "Fórmula de cortesía para introducir una queja o mala noticia ('Me temo que...').",
                "Exchange" to "Cambiar un producto por otro de diferente talla o color."
            )
        )

        return list
    }
}
