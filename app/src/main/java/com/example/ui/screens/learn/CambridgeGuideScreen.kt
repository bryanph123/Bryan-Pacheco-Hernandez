package com.example.ui.screens.learn

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ScreenTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodels.LearnSubScreen
import com.example.ui.viewmodels.MainViewModel

data class GuideGrammarTopic(
    val id: String,
    val level: String,
    val code: String,
    val title: String,
    val explanation: String,
    val examples: List<String>,
    val exerciseType: String,
    val exercisePrompt: String,
    val exerciseOptions: List<String> = emptyList(),
    val correctAnswer: String,
    val acceptableAnswers: List<String> = emptyList()
)

data class GuideVocabCategory(
    val categoryName: String,
    val level: String,
    val items: List<Pair<String, String>>
)

object CambridgeGuideData {
    val cefrExamMapping = listOf(
        Triple("A1", "Pre A1 Starters / A1 Movers", "Base formativa de A2 Key para jóvenes y adultos"),
        Triple("A2", "A2 Key (KET)", "Key English Test - Nivel básico elemental oficial de Cambridge"),
        Triple("B1", "B1 Preliminary (PET)", "Preliminary English Test - Nivel intermedio independiente"),
        Triple("B2", "B2 First (FCE)", "First Certificate in English - Nivel intermedio alto profesional/académico")
    )

    val exerciseTypes = listOf(
        Pair("Multiple-choice cloze", "Elegir la opción correcta (a, b, c o d) para completar un espacio en una oración o texto."),
        Pair("Open cloze", "Completar un espacio sin opciones, deduciendo la palabra gramatical exacta por contexto."),
        Pair("Word formation", "Transformar una palabra base dada (en mayúsculas) mediante prefijos/sufijos para que encaje gramaticalmente."),
        Pair("Key word transformation", "Reescribir una oración usando una palabra clave obligatoria sin alterar el significado original."),
        Pair("Reading multiple choice", "Elegir la respuesta correcta interpretando el propósito o detalle de un texto breve.")
    )

    val grammarTopics = listOf(
        // ================= NIVEL A1 =================
        GuideGrammarTopic(
            id = "g_a1_1",
            level = "A1",
            code = "A1-1",
            title = "Verbo \"to be\" (presente: am/is/are)",
            explanation = "Se usa para describir identidad, estado o características. Forma afirmativa, negativa e interrogativa.",
            examples = listOf("She is a student.", "They aren't tired.", "Are you from Mexico?"),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "My parents ___ at home right now.",
            exerciseOptions = listOf("a) am", "b) is", "c) are", "d) be"),
            correctAnswer = "c) are",
            acceptableAnswers = listOf("c", "are", "c) are")
        ),
        GuideGrammarTopic(
            id = "g_a1_2",
            level = "A1",
            code = "A1-2",
            title = "Pronombres personales y adjetivos posesivos",
            explanation = "Los pronombres (I, you, he, she, it, we, they) reemplazan al sujeto; los posesivos (my, your, his, her, its, our, their) indican pertenencia.",
            examples = listOf("He likes his new bike.", "This is Maria and her mother."),
            exerciseType = "Open cloze",
            exercisePrompt = "This is Ana. ___ (her/she) brother is a teacher.",
            correctAnswer = "Her",
            acceptableAnswers = listOf("her", "she", "Her", "She")
        ),
        GuideGrammarTopic(
            id = "g_a1_3",
            level = "A1",
            code = "A1-3",
            title = "Artículos: a / an / the",
            explanation = "\"a\" y \"an\" para algo no específico (an antes de sonido vocálico); \"the\" para algo específico o ya mencionado.",
            examples = listOf("I have a dog.", "The dog is brown.", "She eats an apple."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "She works in ___ office near her house.",
            exerciseOptions = listOf("a) a", "b) an", "c) the", "d) -"),
            correctAnswer = "b) an",
            acceptableAnswers = listOf("b", "an", "b) an", "a", "a) a")
        ),
        GuideGrammarTopic(
            id = "g_a1_4",
            level = "A1",
            code = "A1-4",
            title = "Plural de los sustantivos",
            explanation = "Regla general +s; +es después de -s,-sh,-ch,-x,-o; cambios en -y → -ies; plurales irregulares (child-children, man-men).",
            examples = listOf("boxes", "babies", "children", "feet"),
            exerciseType = "Open cloze",
            exercisePrompt = "There are three ___ (child) in the park.",
            correctAnswer = "children",
            acceptableAnswers = listOf("children")
        ),
        GuideGrammarTopic(
            id = "g_a1_5",
            level = "A1",
            code = "A1-5",
            title = "Demostrativos: this / that / these / those",
            explanation = "this/these para cosas cercanas (singular/plural); that/those para cosas lejanas (singular/plural).",
            examples = listOf("This book is mine.", "Those shoes are new over there."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "Look at ___ birds over there!",
            exerciseOptions = listOf("a) this", "b) that", "c) these", "d) those"),
            correctAnswer = "d) those",
            acceptableAnswers = listOf("d", "those", "d) those")
        ),
        GuideGrammarTopic(
            id = "g_a1_6",
            level = "A1",
            code = "A1-6",
            title = "Presente simple",
            explanation = "Se usa para rutinas, hechos y verdades generales. Tercera persona singular añade -s/-es.",
            examples = listOf("He works on Mondays.", "She doesn't like coffee.", "Do you play football?"),
            exerciseType = "Word formation",
            exercisePrompt = "She usually ___ (WATCH) TV after dinner.",
            correctAnswer = "watches",
            acceptableAnswers = listOf("watches")
        ),
        GuideGrammarTopic(
            id = "g_a1_7",
            level = "A1",
            code = "A1-7",
            title = "There is / There are",
            explanation = "Expresan existencia. \"There is\" con singular/incontable, \"there are\" con plural.",
            examples = listOf("There is a lamp on the table.", "There are five chairs."),
            exerciseType = "Open cloze",
            exercisePrompt = "___ any milk in the fridge? (question form)",
            correctAnswer = "Is there",
            acceptableAnswers = listOf("is there", "Is there")
        ),
        GuideGrammarTopic(
            id = "g_a1_8",
            level = "A1",
            code = "A1-8",
            title = "Preposiciones de lugar",
            explanation = "in (dentro), on (sobre), under (debajo), next to (al lado de), between (entre), in front of (delante de), behind (detrás de).",
            examples = listOf("The cat is under the table.", "The pen is between the books."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "The book is ___ the table and the lamp.",
            exerciseOptions = listOf("a) between", "b) under", "c) behind", "d) next"),
            correctAnswer = "a) between",
            acceptableAnswers = listOf("a", "between", "a) between")
        ),
        GuideGrammarTopic(
            id = "g_a1_9",
            level = "A1",
            code = "A1-9",
            title = "Preposiciones de tiempo: at / in / on",
            explanation = "at + hora (at 6 o'clock); in + mes/año/estación (in July); on + día/fecha (on Monday).",
            examples = listOf("I get up at 7.", "My birthday is in June.", "We have class on Friday."),
            exerciseType = "Open cloze",
            exercisePrompt = "The meeting is ___ Monday morning.",
            correctAnswer = "on",
            acceptableAnswers = listOf("on", "On")
        ),
        GuideGrammarTopic(
            id = "g_a1_10",
            level = "A1",
            code = "A1-10",
            title = "Verbo modal \"can\" (habilidad / permiso)",
            explanation = "\"can\" + infinitivo sin \"to\" expresa habilidad o permiso.",
            examples = listOf("She can swim.", "Can I open the window, please?"),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "He ___ speak three languages.",
            exerciseOptions = listOf("a) can", "b) cans", "c) is can", "d) can to"),
            correctAnswer = "a) can",
            acceptableAnswers = listOf("a", "can", "a) can")
        ),
        GuideGrammarTopic(
            id = "g_a1_11",
            level = "A1",
            code = "A1-11",
            title = "Palabras interrogativas (Wh- questions) e imperativos",
            explanation = "what, where, when, who, why, how se usan para preguntar; los imperativos dan órdenes o instrucciones (verbo sin sujeto).",
            examples = listOf("Where do you live?", "Close the door, please."),
            exerciseType = "Open cloze",
            exercisePrompt = "___ is your English teacher? (pregunta por persona)",
            correctAnswer = "Who",
            acceptableAnswers = listOf("who", "Who")
        ),

        // ================= NIVEL A2 =================
        GuideGrammarTopic(
            id = "g_a2_1",
            level = "A2",
            code = "A2-1",
            title = "Presente continuo",
            explanation = "Describe acciones que ocurren ahora mismo o de forma temporal. Verbo \"to be\" + verbo-ing.",
            examples = listOf("I am studying English this year.", "She is cooking right now."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "Look! It ___ outside.",
            exerciseOptions = listOf("a) rain", "b) rains", "c) is raining", "d) raining"),
            correctAnswer = "c) is raining",
            acceptableAnswers = listOf("c", "is raining", "c) is raining")
        ),
        GuideGrammarTopic(
            id = "g_a2_2",
            level = "A2",
            code = "A2-2",
            title = "Pasado simple: verbos regulares e irregulares",
            explanation = "Regulares +ed; irregulares tienen forma propia (go→went, see→saw). Negativo/interrogativo con \"did\".",
            examples = listOf("She visited her grandmother.", "They didn't go to the party.", "Did you see the film?"),
            exerciseType = "Word formation",
            exercisePrompt = "We ___ (WATCH) a great documentary last night.",
            correctAnswer = "watched",
            acceptableAnswers = listOf("watched")
        ),
        GuideGrammarTopic(
            id = "g_a2_3",
            level = "A2",
            code = "A2-3",
            title = "Comparativos y superlativos",
            explanation = "Adjetivos cortos +er/+est; adjetivos largos more/most; formas irregulares (good-better-best).",
            examples = listOf("This city is bigger than mine.", "It's the most beautiful place I've seen."),
            exerciseType = "Open cloze",
            exercisePrompt = "My brother is ___ (tall) than me.",
            correctAnswer = "taller",
            acceptableAnswers = listOf("taller")
        ),
        GuideGrammarTopic(
            id = "g_a2_4",
            level = "A2",
            code = "A2-4",
            title = "Contables/incontables + some/any",
            explanation = "Contables tienen plural (an apple/apples); incontables no (water, rice). \"Some\" en afirmativas, \"any\" en negativas/preguntas.",
            examples = listOf("There is some bread.", "There isn't any milk.", "Is there any sugar?"),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "Can I have ___ water, please?",
            exerciseOptions = listOf("a) a", "b) some", "c) any", "d) many"),
            correctAnswer = "b) some",
            acceptableAnswers = listOf("b", "some", "b) some")
        ),
        GuideGrammarTopic(
            id = "g_a2_5",
            level = "A2",
            code = "A2-5",
            title = "Cuantificadores: much / many / a lot of / a few / a little",
            explanation = "\"many\" con contables, \"much\" con incontables (sobre todo en negativas/preguntas), \"a lot of\" en afirmativas; \"a few\" (contables) y \"a little\" (incontables) para poca cantidad.",
            examples = listOf("There are a lot of books.", "I don't have much time.", "She has a few friends here."),
            exerciseType = "Open cloze",
            exercisePrompt = "How ___ money do you have?",
            correctAnswer = "much",
            acceptableAnswers = listOf("much", "Much")
        ),
        GuideGrammarTopic(
            id = "g_a2_6",
            level = "A2",
            code = "A2-6",
            title = "Futuro con \"going to\"",
            explanation = "Expresa planes o predicciones basadas en evidencia. \"to be\" + going to + infinitivo.",
            examples = listOf("We are going to travel next month.", "Look at those clouds — it's going to rain."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "She ___ study medicine next year.",
            exerciseOptions = listOf("a) go to", "b) is going to", "c) going", "d) goes to"),
            correctAnswer = "b) is going to",
            acceptableAnswers = listOf("b", "is going to", "b) is going to")
        ),
        GuideGrammarTopic(
            id = "g_a2_7",
            level = "A2",
            code = "A2-7",
            title = "Verbos modales: must / have to / should",
            explanation = "\"must/have to\" expresan obligación; \"should\" expresa recomendación o consejo.",
            examples = listOf("You must wear a seatbelt.", "I have to finish this today.", "You should drink more water."),
            exerciseType = "Open cloze",
            exercisePrompt = "Students ___ (obligación) bring their ID to the exam.",
            correctAnswer = "must",
            acceptableAnswers = listOf("must", "have to", "Must", "Have to")
        ),
        GuideGrammarTopic(
            id = "g_a2_8",
            level = "A2",
            code = "A2-8",
            title = "Presente perfecto (introducción)",
            explanation = "Conecta el pasado con el presente; se usa con ever, never, just, already, yet. Have/has + participio.",
            examples = listOf("Have you ever been to London?", "I've just finished my homework."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "She ___ never eaten sushi.",
            exerciseOptions = listOf("a) have", "b) has", "c) is", "d) did"),
            correctAnswer = "b) has",
            acceptableAnswers = listOf("b", "has", "b) has")
        ),
        GuideGrammarTopic(
            id = "g_a2_9",
            level = "A2",
            code = "A2-9",
            title = "Adverbios de frecuencia",
            explanation = "always, usually, often, sometimes, rarely, never; van antes del verbo principal (pero después de \"to be\").",
            examples = listOf("He always arrives early.", "She is never late."),
            exerciseType = "Open cloze",
            exercisePrompt = "I ___ (frecuencia media: sometimes/often) go to the gym on weekends.",
            correctAnswer = "sometimes",
            acceptableAnswers = listOf("sometimes", "often", "usually", "Sometimes", "Often")
        ),
        GuideGrammarTopic(
            id = "g_a2_10",
            level = "A2",
            code = "A2-10",
            title = "Pronombres objeto y posesivos",
            explanation = "Pronombres objeto (me, you, him, her, it, us, them); pronombres posesivos (mine, yours, his, hers, ours, theirs).",
            examples = listOf("Can you help me?", "This book is mine, not yours."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "Is this pen yours or ___?",
            exerciseOptions = listOf("a) her", "b) hers", "c) she", "d) she's"),
            correctAnswer = "b) hers",
            acceptableAnswers = listOf("b", "hers", "b) hers")
        ),

        // ================= NIVEL B1 =================
        GuideGrammarTopic(
            id = "g_b1_1",
            level = "B1",
            code = "B1-1",
            title = "Pasado continuo",
            explanation = "Describe una acción en desarrollo en un momento del pasado, a menudo interrumpida por otra acción en pasado simple.",
            examples = listOf("I was studying when the phone rang.", "What were you doing at 8 PM?"),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "They ___ dinner when the lights went out.",
            exerciseOptions = listOf("a) cooked", "b) were cooking", "c) cook", "d) are cooking"),
            correctAnswer = "b) were cooking",
            acceptableAnswers = listOf("b", "were cooking", "b) were cooking")
        ),
        GuideGrammarTopic(
            id = "g_b1_2",
            level = "B1",
            code = "B1-2",
            title = "Presente perfecto vs. pasado simple",
            explanation = "Presente perfecto para experiencias/acciones sin tiempo específico; pasado simple para acciones terminadas en un momento concreto.",
            examples = listOf("I have visited Paris twice.", "I visited Paris in 2019."),
            exerciseType = "Open cloze",
            exercisePrompt = "She ___ (live) in Canada for two years before she moved back.",
            correctAnswer = "had lived",
            acceptableAnswers = listOf("had lived", "lived")
        ),
        GuideGrammarTopic(
            id = "g_b1_3",
            level = "B1",
            code = "B1-3",
            title = "Formas de futuro: will / going to / presente continuo",
            explanation = "\"will\" para decisiones espontáneas y predicciones; \"going to\" para planes; presente continuo para citas ya organizadas.",
            examples = listOf("I'll help you with that.", "We're meeting the dentist at 5pm on Friday."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "I think it ___ rain later.",
            exerciseOptions = listOf("a) will", "b) is going to", "c) is", "d) going"),
            correctAnswer = "a) will",
            acceptableAnswers = listOf("a", "will", "a) will")
        ),
        GuideGrammarTopic(
            id = "g_b1_4",
            level = "B1",
            code = "B1-4",
            title = "Primer condicional",
            explanation = "Situaciones reales/posibles en el futuro. If + presente simple, will + infinitivo.",
            examples = listOf("If it rains, we will stay home.", "If you hurry, you will catch the train."),
            exerciseType = "Open cloze",
            exercisePrompt = "If you ___ (study) hard, you will pass the exam.",
            correctAnswer = "study",
            acceptableAnswers = listOf("study")
        ),
        GuideGrammarTopic(
            id = "g_b1_5",
            level = "B1",
            code = "B1-5",
            title = "Segundo condicional",
            explanation = "Situaciones hipotéticas o poco probables en el presente/futuro. If + pasado simple, would + infinitivo.",
            examples = listOf("If I won the lottery, I would travel the world.", "If I were you, I would take that opportunity."),
            exerciseType = "Key word transformation",
            exercisePrompt = "I don't have a car, so I don't drive to work. (IF) → If I had a car, I would drive to work.",
            correctAnswer = "I had a car, I would drive to work",
            acceptableAnswers = listOf("i had a car, i would drive to work", "if i had a car, i would drive to work", "I had a car, I would drive to work")
        ),
        GuideGrammarTopic(
            id = "g_b1_6",
            level = "B1",
            code = "B1-6",
            title = "Modales de posibilidad: may / might / could",
            explanation = "Expresan posibilidad, no certeza.",
            examples = listOf("She might be at home.", "It could rain later."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "He isn't here yet; he ___ be stuck in traffic.",
            exerciseOptions = listOf("a) must", "b) might", "c) can", "d) should"),
            correctAnswer = "b) might",
            acceptableAnswers = listOf("b", "might", "b) might", "could")
        ),
        GuideGrammarTopic(
            id = "g_b1_7",
            level = "B1",
            code = "B1-7",
            title = "Voz pasiva (presente y pasado simple)",
            explanation = "Se usa cuando la acción importa más que quién la hace. Objeto + to be + participio (+ by + agente).",
            examples = listOf("The letter was written by Ana.", "English is spoken in many countries."),
            exerciseType = "Open cloze",
            exercisePrompt = "This bridge ___ (build) in 1990.",
            correctAnswer = "was built",
            acceptableAnswers = listOf("was built")
        ),
        GuideGrammarTopic(
            id = "g_b1_8",
            level = "B1",
            code = "B1-8",
            title = "Estilo indirecto básico (reported speech)",
            explanation = "Al reportar lo que alguien dijo, los tiempos verbales suelen retroceder un paso (presente→pasado, etc.).",
            examples = listOf("\"I am tired,\" she said. → She said (that) she was tired."),
            exerciseType = "Key word transformation",
            exercisePrompt = "\"I will call you tomorrow,\" he said. (TOLD) → He told me he would call me the next day.",
            correctAnswer = "he would call me the next day",
            acceptableAnswers = listOf("he would call me the next day", "that he would call me the next day")
        ),
        GuideGrammarTopic(
            id = "g_b1_9",
            level = "B1",
            code = "B1-9",
            title = "Oraciones de relativo definitorias",
            explanation = "Dan información esencial sobre el sustantivo, usando who (personas), which (cosas), that (ambos), where (lugares).",
            examples = listOf("The man who lives next door is a doctor.", "The book which is on the desk is mine."),
            exerciseType = "Open cloze",
            exercisePrompt = "This is the restaurant ___ we had our first date.",
            correctAnswer = "where",
            acceptableAnswers = listOf("where")
        ),
        GuideGrammarTopic(
            id = "g_b1_10",
            level = "B1",
            code = "B1-10",
            title = "Gerundio vs. infinitivo",
            explanation = "Algunos verbos van seguidos de gerundio (enjoy, avoid, finish) y otros de infinitivo con \"to\" (want, decide, hope).",
            examples = listOf("I enjoy reading.", "She wants to travel."),
            exerciseType = "Word formation",
            exercisePrompt = "He decided ___ (LEAVE) early.",
            correctAnswer = "to leave",
            acceptableAnswers = listOf("to leave")
        ),
        GuideGrammarTopic(
            id = "g_b1_11",
            level = "B1",
            code = "B1-11",
            title = "Phrasal verbs comunes",
            explanation = "Verbo + partícula con significado propio (a veces distinto del literal).",
            examples = listOf("give up (rendirse)", "look for (buscar)", "find out (descubrir)", "get on (llevarse bien)", "turn off (apagar)"),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "Please ___ the lights before you leave.",
            exerciseOptions = listOf("a) turn off", "b) turn on", "c) turn up", "d) turn into"),
            correctAnswer = "a) turn off",
            acceptableAnswers = listOf("a", "turn off", "a) turn off")
        ),

        // ================= NIVEL B2 =================
        GuideGrammarTopic(
            id = "g_b2_1",
            level = "B2",
            code = "B2-1",
            title = "Tercer condicional",
            explanation = "Situaciones hipotéticas en el pasado, imposibles de cambiar. If + pluscuamperfecto, would have + participio.",
            examples = listOf("If I had known, I would have told you.", "If they had arrived earlier, they wouldn't have missed the flight."),
            exerciseType = "Open cloze",
            exercisePrompt = "If she ___ (study) harder, she would have passed the test.",
            correctAnswer = "had studied",
            acceptableAnswers = listOf("had studied")
        ),
        GuideGrammarTopic(
            id = "g_b2_2",
            level = "B2",
            code = "B2-2",
            title = "Condicionales mixtos (introducción)",
            explanation = "Combinan una condición pasada con un resultado presente, o una condición presente con un resultado pasado.",
            examples = listOf("If I hadn't missed the flight, I would be there now.", "If he spoke French, he would have translated the document."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "If he weren't so lazy, he ___ the job.",
            exerciseOptions = listOf("a) would get", "b) would have got", "c) will get", "d) gets"),
            correctAnswer = "a) would get",
            acceptableAnswers = listOf("a", "would get", "a) would get", "b", "would have got")
        ),
        GuideGrammarTopic(
            id = "g_b2_3",
            level = "B2",
            code = "B2-3",
            title = "Voz pasiva en todos los tiempos",
            explanation = "Se puede formar en cualquier tiempo verbal (presente perfecto, futuro, modales, etc.).",
            examples = listOf("The report has been finished.", "The problem will be solved soon."),
            exerciseType = "Open cloze",
            exercisePrompt = "The new bridge ___ (open) next month.",
            correctAnswer = "will be opened",
            acceptableAnswers = listOf("will be opened", "will open", "is going to be opened")
        ),
        GuideGrammarTopic(
            id = "g_b2_4",
            level = "B2",
            code = "B2-4",
            title = "Estilo indirecto avanzado",
            explanation = "Incluye preguntas (con if/whether o palabra interrogativa) y órdenes (told + object + to + infinitive).",
            examples = listOf("\"Are you coming?\" she asked. → She asked if I was coming.", "\"Sit down,\" he said. → He told them to sit down."),
            exerciseType = "Key word transformation",
            exercisePrompt = "\"Don't touch that!\" she said to the children. (NOT) → She told the children not to touch that.",
            correctAnswer = "not to touch that",
            acceptableAnswers = listOf("not to touch that", "to not touch that")
        ),
        GuideGrammarTopic(
            id = "g_b2_5",
            level = "B2",
            code = "B2-5",
            title = "Oraciones de relativo no definitorias",
            explanation = "Añaden información extra (no esencial), separadas por comas; no se usa \"that\".",
            examples = listOf("My father, who is 70, still works part-time.", "London, which is the capital of the UK, is very vibrant."),
            exerciseType = "Open cloze",
            exercisePrompt = "Mexico City, ___ is the capital of Mexico, has over 20 million inhabitants.",
            correctAnswer = "which",
            acceptableAnswers = listOf("which")
        ),
        GuideGrammarTopic(
            id = "g_b2_6",
            level = "B2",
            code = "B2-6",
            title = "Modales de deducción: must have / can't have / might have",
            explanation = "Expresan certeza (must have), imposibilidad (can't have) o posibilidad (might/could have) sobre el pasado.",
            examples = listOf("She isn't answering; she must have forgotten her phone.", "He can't have seen you; he was abroad."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "He ___ left already — his car isn't here.",
            exerciseOptions = listOf("a) must have", "b) can't have", "c) should", "d) must"),
            correctAnswer = "a) must have",
            acceptableAnswers = listOf("a", "must have", "a) must have")
        ),
        GuideGrammarTopic(
            id = "g_b2_7",
            level = "B2",
            code = "B2-7",
            title = "Presente perfecto continuo",
            explanation = "Enfatiza la duración de una acción que empezó en el pasado y continúa (o acaba de terminar).",
            examples = listOf("I've been working here for five years.", "She's been crying — her eyes are red."),
            exerciseType = "Open cloze",
            exercisePrompt = "They ___ (wait) for the bus for twenty minutes.",
            correctAnswer = "have been waiting",
            acceptableAnswers = listOf("have been waiting", "'ve been waiting")
        ),
        GuideGrammarTopic(
            id = "g_b2_8",
            level = "B2",
            code = "B2-8",
            title = "Pasado perfecto simple y continuo",
            explanation = "La acción anterior a otra acción pasada. Simple para hechos completados; continuo para énfasis en la duración.",
            examples = listOf("When I arrived, the film had already started.", "She was tired because she had been running."),
            exerciseType = "Open cloze",
            exercisePrompt = "By the time we got to the station, the train ___ (leave).",
            correctAnswer = "had left",
            acceptableAnswers = listOf("had left")
        ),
        GuideGrammarTopic(
            id = "g_b2_9",
            level = "B2",
            code = "B2-9",
            title = "Used to / would (hábitos pasados)",
            explanation = "\"used to\" para hábitos y estados pasados que ya no ocurren; \"would\" solo para hábitos repetidos (no estados).",
            examples = listOf("I used to live in Puebla.", "Every summer, we would visit my grandparents."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "She ___ be very shy when she was a child.",
            exerciseOptions = listOf("a) would", "b) used to", "c) use to", "d) was used"),
            correctAnswer = "b) used to",
            acceptableAnswers = listOf("b", "used to", "b) used to")
        ),
        GuideGrammarTopic(
            id = "g_b2_10",
            level = "B2",
            code = "B2-10",
            title = "Wish / If only",
            explanation = "Expresan deseo de que algo presente fuera distinto (wish + pasado simple) o arrepentimiento por el pasado (wish + pluscuamperfecto).",
            examples = listOf("I wish I had more free time.", "If only I had studied more for the exam."),
            exerciseType = "Open cloze",
            exercisePrompt = "I wish I ___ (know) the answer.",
            correctAnswer = "knew",
            acceptableAnswers = listOf("knew")
        ),
        GuideGrammarTopic(
            id = "g_b2_11",
            level = "B2",
            code = "B2-11",
            title = "Conectores avanzados",
            explanation = "although/though (aunque, + oración), despite/in spite of (a pesar de, + sustantivo/gerundio), however/nevertheless (sin embargo, al inicio de oración).",
            examples = listOf("Although it was raining, we went out.", "Despite the traffic, we arrived on time."),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "___ the difficulties, the project was a success.",
            exerciseOptions = listOf("a) Although", "b) Despite", "c) However", "d) Because"),
            correctAnswer = "b) Despite",
            acceptableAnswers = listOf("b", "despite", "b) despite", "Despite")
        ),
        GuideGrammarTopic(
            id = "g_b2_12",
            level = "B2",
            code = "B2-12",
            title = "Phrasal verbs y expresiones idiomáticas",
            explanation = "En B2 aumenta el número y la variedad, incluyendo expresiones con significado no literal.",
            examples = listOf("come up with (idear)", "get away with (salirse con la suya)", "put up with (aguantar)", "break the ice", "once in a blue moon"),
            exerciseType = "Multiple-choice cloze",
            exercisePrompt = "I don't know how she ___ such a great idea.",
            exerciseOptions = listOf("a) came up with", "b) came up", "c) came into", "d) came over"),
            correctAnswer = "a) came up with",
            acceptableAnswers = listOf("a", "came up with", "a) came up with")
        )
    )

    val vocabCategories = listOf(
        // ================= A1 VOCABULARY (14 TEMAS) =================
        GuideVocabCategory(
            categoryName = "Familia",
            level = "A1",
            items = listOf(
                "parents" to "padres", "relatives" to "parientes", "grandparents" to "abuelos",
                "married" to "casado/a", "single" to "soltero/a", "baby" to "bebé",
                "teenager" to "adolescente", "adult" to "adulto", "twin" to "gemelo/a",
                "friend" to "amigo/a", "mother" to "madre", "father" to "padre",
                "brother" to "hermano", "sister" to "hermana", "son" to "hijo", "daughter" to "hija"
            )
        ),
        GuideVocabCategory(
            categoryName = "Colores",
            level = "A1",
            items = listOf(
                "red" to "rojo", "blue" to "azul", "green" to "verde", "yellow" to "amarillo",
                "black" to "negro", "white" to "blanco", "orange" to "naranja", "purple" to "morado/púrpura",
                "pink" to "rosa", "brown" to "café/marrón", "grey" to "gris", "gold" to "dorado/oro", "silver" to "plateado/plata"
            )
        ),
        GuideVocabCategory(
            categoryName = "Números y cantidades",
            level = "A1",
            items = listOf(
                "one to twenty" to "uno al veinte (1–20)", "thirty" to "treinta (30)", "forty" to "cuarenta (40)",
                "fifty" to "cincuenta (50)", "sixty" to "sesenta (60)", "seventy" to "setenta (70)",
                "eighty" to "ochenta (80)", "ninety" to "noventa (90)", "one hundred" to "cien (100)",
                "first" to "primero", "second" to "segundo", "third" to "tercero",
                "a lot of" to "mucho/a", "a little" to "poco/a"
            )
        ),
        GuideVocabCategory(
            categoryName = "Días, meses y estaciones",
            level = "A1",
            items = listOf(
                "Monday to Sunday" to "lunes a domingo", "January to December" to "enero a diciembre",
                "spring" to "primavera", "summer" to "verano", "autumn" to "otoño", "winter" to "invierno",
                "today" to "hoy", "tomorrow" to "mañana", "yesterday" to "ayer",
                "week" to "semana", "month" to "mes", "year" to "año"
            )
        ),
        GuideVocabCategory(
            categoryName = "La hora",
            level = "A1",
            items = listOf(
                "o'clock" to "en punto", "half past" to "y media", "quarter past" to "y cuarto",
                "quarter to" to "cuarto para / menos cuarto", "morning" to "mañana", "afternoon" to "tarde",
                "evening" to "tarde-noche / noche", "night" to "noche", "early" to "temprano", "late" to "tarde"
            )
        ),
        GuideVocabCategory(
            categoryName = "Comida y bebida",
            level = "A1",
            items = listOf(
                "breakfast" to "desayuno", "lunch" to "comida/almuerzo", "dinner" to "cena",
                "snack" to "merienda/refrigerio", "meal" to "comida (tiempo)", "hungry" to "con hambre",
                "thirsty" to "con sed", "bread" to "pan", "rice" to "arroz", "chicken" to "pollo",
                "apple" to "manzana", "milk" to "leche", "water" to "agua", "egg" to "huevo",
                "cheese" to "queso", "vegetable" to "verdura", "fruit" to "fruta",
                "meat" to "carne", "fish" to "pescado", "sugar" to "azúcar", "salt" to "sal"
            )
        ),
        GuideVocabCategory(
            categoryName = "La casa y los muebles",
            level = "A1",
            items = listOf(
                "house" to "casa", "flat" to "departamento/piso", "room" to "habitación",
                "kitchen" to "cocina", "bedroom" to "dormitorio/recámara", "bathroom" to "baño",
                "living room" to "sala de estar", "garden" to "jardín", "bed" to "cama",
                "sofa" to "sofá", "table" to "mesa", "chair" to "silla",
                "television" to "televisión", "fridge" to "refrigerador", "door" to "puerta", "window" to "ventana"
            )
        ),
        GuideVocabCategory(
            categoryName = "La ropa",
            level = "A1",
            items = listOf(
                "t-shirt" to "playera/camiseta", "shirt" to "camisa", "trousers" to "pantalones",
                "dress" to "vestido", "jacket" to "chamarra/chaqueta", "shoes" to "zapatos",
                "socks" to "calcetines", "hat" to "sombrero/gorro", "coat" to "abrigo", "skirt" to "falda"
            )
        ),
        GuideVocabCategory(
            categoryName = "El cuerpo humano",
            level = "A1",
            items = listOf(
                "head" to "cabeza", "hand" to "mano", "arm" to "brazo", "leg" to "pierna",
                "foot" to "pie", "eye" to "ojo", "ear" to "oreja/oído", "nose" to "nariz",
                "mouth" to "boca", "hair" to "cabello/pelo", "face" to "cara"
            )
        ),
        GuideVocabCategory(
            categoryName = "Profesiones y lugares de trabajo",
            level = "A1",
            items = listOf(
                "teacher" to "profesor/a", "doctor" to "médico/a", "nurse" to "enfermero/a",
                "engineer" to "ingeniero/a", "farmer" to "agricultor/granjero", "driver" to "conductor/a",
                "cook" to "cocinero/a", "police officer" to "oficial de policía", "office" to "oficina",
                "hospital" to "hospital", "school" to "escuela", "shop" to "tienda"
            )
        ),
        GuideVocabCategory(
            categoryName = "Objetos de la escuela",
            level = "A1",
            items = listOf(
                "book" to "libro", "pen" to "pluma/bolígrafo", "pencil" to "lápiz", "table" to "mesa",
                "chair" to "silla", "board" to "pizarrón/pizarra", "notebook" to "cuaderno",
                "eraser" to "borrador/goma", "ruler" to "regla", "bag" to "mochila/bolsa", "classroom" to "salón de clases/aula"
            )
        ),
        GuideVocabCategory(
            categoryName = "Animales",
            level = "A1",
            items = listOf(
                "dog" to "perro", "cat" to "gato", "bird" to "pájaro/ave", "fish" to "pez",
                "horse" to "caballo", "cow" to "vaca", "pig" to "cerdo", "rabbit" to "conejo",
                "mouse" to "ratón", "sheep" to "oveja"
            )
        ),
        GuideVocabCategory(
            categoryName = "Verbos de acción básicos",
            level = "A1",
            items = listOf(
                "go" to "ir", "come" to "venir", "eat" to "comer", "drink" to "beber/tomar",
                "sleep" to "dormir", "play" to "jugar/tocar", "read" to "leer", "write" to "escribir",
                "listen" to "escuchar", "watch" to "mirar/ver", "walk" to "caminar", "run" to "correr",
                "sit" to "sentarse", "stand" to "estar de pie", "open" to "abrir", "close" to "cerrar",
                "live" to "vivir", "work" to "trabajar", "study" to "estudiar", "help" to "ayudar"
            )
        ),
        GuideVocabCategory(
            categoryName = "Adjetivos básicos",
            level = "A1",
            items = listOf(
                "big" to "grande", "small" to "pequeño", "happy" to "feliz", "sad" to "triste",
                "hot" to "caliente/caluroso", "cold" to "frío", "good" to "bueno", "bad" to "malo",
                "easy" to "fácil", "difficult" to "difícil", "new" to "nuevo", "old" to "viejo",
                "fast" to "rápido", "slow" to "lento", "tall" to "alto", "short" to "bajo/corto",
                "long" to "largo", "clean" to "limpio", "dirty" to "sucio"
            )
        ),

        // ================= A2 VOCABULARY (12 TEMAS) =================
        GuideVocabCategory(
            categoryName = "Ropa y accesorios",
            level = "A2",
            items = listOf(
                "jewellery" to "joyería", "scarf" to "bufanda", "gloves" to "guantes", "boots" to "botas",
                "sunglasses" to "lentes de sol", "umbrella" to "paraguas", "belt" to "cinturón", "uniform" to "uniforme"
            )
        ),
        GuideVocabCategory(
            categoryName = "Clima",
            level = "A2",
            items = listOf(
                "sunny" to "soleado", "rainy" to "lluvioso", "cloudy" to "nublado", "windy" to "ventoso",
                "snowy" to "nevado", "cold" to "frío", "hot" to "caluroso", "warm" to "cálido/templado",
                "storm" to "tormenta", "fog" to "niebla", "temperature" to "temperatura", "forecast" to "pronóstico del tiempo"
            )
        ),
        GuideVocabCategory(
            categoryName = "Viajes y transporte",
            level = "A2",
            items = listOf(
                "airport" to "aeropuerto", "luggage" to "equipaje", "delay" to "retraso",
                "boarding pass" to "pase de abordar", "platform" to "andén", "ticket office" to "taquilla/boletería",
                "tour" to "recorrido/tour", "destination" to "destino", "means of transport" to "medios de transporte",
                "bus" to "autobús/camión", "train" to "tren", "plane" to "avión", "ship" to "barco",
                "taxi" to "taxi", "bike" to "bicicleta", "journey" to "viaje/trayecto", "passport" to "pasaporte", "suitcase" to "maleta"
            )
        ),
        GuideVocabCategory(
            categoryName = "Salud y el cuerpo",
            level = "A2",
            items = listOf(
                "symptom" to "síntoma", "illness" to "enfermedad", "pharmacy" to "farmacia",
                "prescription" to "receta médica", "injury" to "lesión", "hospital" to "hospital",
                "dentist" to "dentista", "headache" to "dolor de cabeza", "stomachache" to "dolor de estómago",
                "fever" to "fiebre", "cough" to "tos", "sore throat" to "dolor de garganta",
                "medicine" to "medicina", "appointment" to "cita médica"
            )
        ),
        GuideVocabCategory(
            categoryName = "Pasatiempos y deportes",
            level = "A2",
            items = listOf(
                "football" to "fútbol", "basketball" to "baloncesto", "tennis" to "tenis",
                "swimming" to "natación", "running" to "correr/carrera", "painting" to "pintura",
                "cooking" to "cocina", "gardening" to "jardinería", "photography" to "fotografía",
                "chess" to "ajedrez", "team" to "equipo", "match" to "partido", "competition" to "competición/torneo"
            )
        ),
        GuideVocabCategory(
            categoryName = "Compras y dinero",
            level = "A2",
            items = listOf(
                "shop" to "tienda", "market" to "mercado", "mall" to "centro comercial",
                "price" to "precio", "discount" to "descuento", "receipt" to "recibo/ticket",
                "cash" to "efectivo", "credit card" to "tarjeta de crédito", "expensive" to "caro",
                "cheap" to "barato", "sale" to "rebaja/oferta", "size" to "talla/tamaño", "changing room" to "probador"
            )
        ),
        GuideVocabCategory(
            categoryName = "La casa (ampliado)",
            level = "A2",
            items = listOf(
                "appliance" to "electrodoméstico", "washing machine" to "lavadora", "oven" to "horno",
                "microwave" to "microondas", "vacuum cleaner" to "aspiradora", "shelf" to "estante/repisa",
                "wardrobe" to "armario/ropero", "curtain" to "cortina", "carpet" to "alfombra"
            )
        ),
        GuideVocabCategory(
            categoryName = "Trabajo y estudios",
            level = "A2",
            items = listOf(
                "job" to "trabajo/empleo", "office" to "oficina", "boss" to "jefe/a",
                "colleague" to "colega/compañero", "salary" to "salario/sueldo", "homework" to "tarea",
                "subject" to "materia/asignatura", "timetable" to "horario", "exam" to "examen",
                "degree" to "título/grado", "university" to "universidad", "classmate" to "compañero/a de clase"
            )
        ),
        GuideVocabCategory(
            categoryName = "Comida y restaurantes",
            level = "A2",
            items = listOf(
                "menu" to "menú/carta", "order" to "pedir/orden", "waiter" to "mesero/camarero",
                "bill" to "cuenta", "recipe" to "receta", "ingredient" to "ingrediente",
                "starter" to "entrada/primer plato", "main course" to "plato fuerte/principal",
                "dessert" to "postre", "delicious" to "delicioso", "fresh" to "fresco"
            )
        ),
        GuideVocabCategory(
            categoryName = "Adjetivos descriptivos (ampliado)",
            level = "A2",
            items = listOf(
                "tall" to "alto", "short" to "bajo/corto", "thin" to "delgado",
                "heavy" to "pesado", "light" to "ligero/liviano", "expensive" to "caro",
                "cheap" to "barato", "comfortable" to "cómodo", "dangerous" to "peligroso",
                "interesting" to "interesante", "boring" to "aburrido", "crowded" to "abarrotado/concurrido",
                "quiet" to "tranquilo/silencioso", "noisy" to "ruidoso"
            )
        ),
        GuideVocabCategory(
            categoryName = "Verbos de rutina",
            level = "A2",
            items = listOf(
                "get up" to "levantarse", "have breakfast" to "desayunar", "go to work" to "ir al trabajo",
                "come back" to "regresar/volver", "do homework" to "hacer la tarea", "make dinner" to "preparar la cena",
                "take a shower" to "bañarse/ducharse", "brush your teeth" to "cepillarse los dientes",
                "go shopping" to "ir de compras", "catch a bus" to "tomar el autobús"
            )
        ),
        GuideVocabCategory(
            categoryName = "Emociones básicas",
            level = "A2",
            items = listOf(
                "happy" to "feliz", "sad" to "triste", "angry" to "enojado/enfadado",
                "tired" to "cansado", "bored" to "aburrido", "excited" to "emocionado",
                "worried" to "preocupado", "surprised" to "sorprendido", "scared" to "asustado", "relaxed" to "relajado"
            )
        ),

        // ================= B1 VOCABULARY (10 TEMAS) =================
        GuideVocabCategory(
            categoryName = "Medio ambiente",
            level = "B1",
            items = listOf(
                "pollution" to "contaminación", "recycling" to "reciclaje", "climate change" to "cambio climático",
                "natural resources" to "recursos naturales", "endangered animals" to "animales en peligro de extinción",
                "protect" to "proteger", "environmentally friendly" to "ecológico/amigable con el ambiente",
                "renewable energy" to "energía renovable", "deforestation" to "deforestación",
                "greenhouse gas" to "gas de efecto invernadero", "waste" to "residuos/desperdicio",
                "sustainability" to "sostenibilidad"
            )
        ),
        GuideVocabCategory(
            categoryName = "Tecnología",
            level = "B1",
            items = listOf(
                "device" to "dispositivo", "app" to "aplicación móvil", "wifi" to "red wifi",
                "password" to "contraseña", "screen" to "pantalla", "charger" to "cargador",
                "social media" to "redes sociales", "online" to "en línea/conectado", "download" to "descargar",
                "upload" to "subir/cargar", "wireless" to "inalámbrico", "battery" to "batería",
                "artificial intelligence" to "inteligencia artificial"
            )
        ),
        GuideVocabCategory(
            categoryName = "Educación",
            level = "B1",
            items = listOf(
                "curriculum" to "plan de estudios", "degree" to "título universitario", "scholarship" to "beca",
                "exam board" to "comité examinador", "assignment" to "tarea/proyecto", "lecture" to "conferencia/clase magistral",
                "campus" to "campus universitario", "graduate" to "graduarse/graduado", "tutor" to "tutor",
                "qualification" to "cualificación/título"
            )
        ),
        GuideVocabCategory(
            categoryName = "Sentimientos y emociones",
            level = "B1",
            items = listOf(
                "anxious" to "ansioso/a", "disappointed" to "decepcionado/a", "frustrated" to "frustrado/a",
                "relieved" to "aliviado/a", "embarrassed" to "avergonzado/a", "confident" to "seguro/confiado",
                "nervous" to "nervioso/a", "proud" to "orgulloso/a", "jealous" to "celoso/a", "ashamed" to "avergonzado/a"
            )
        ),
        GuideVocabCategory(
            categoryName = "Describir el carácter",
            level = "B1",
            items = listOf(
                "hard-working" to "trabajador/a", "generous" to "generoso/a", "stubborn" to "terco/a",
                "reliable" to "confiable", "honest" to "honesto/a", "ambitious" to "ambicioso/a",
                "patient" to "paciente", "shy" to "tímido/a", "friendly" to "amable/amistoso",
                "selfish" to "egoísta", "easy-going" to "relajado/tratable", "sensitive" to "sensible"
            )
        ),
        GuideVocabCategory(
            categoryName = "Relaciones personales",
            level = "B1",
            items = listOf(
                "relationship" to "relación", "friendship" to "amistad", "colleague" to "colega",
                "neighbour" to "vecino/a", "acquaintance" to "conocido/a", "get on well" to "llevarse bien",
                "fall out" to "pelearse/distanciarse", "trust" to "confiar/confianza", "support" to "apoyar/apoyo"
            )
        ),
        GuideVocabCategory(
            categoryName = "Ciudad y comunidad",
            level = "B1",
            items = listOf(
                "neighbourhood" to "vecindario/colonia", "community" to "comunidad", "facilities" to "instalaciones/servicios",
                "public transport" to "transporte público", "traffic" to "tráfico", "crime rate" to "tasa de criminalidad",
                "population" to "población", "suburb" to "suburbio/afueras", "pedestrian" to "peatón"
            )
        ),
        GuideVocabCategory(
            categoryName = "El trabajo",
            level = "B1",
            items = listOf(
                "career" to "carrera profesional", "interview" to "entrevista de trabajo", "CV" to "currículum",
                "employer" to "empleador/patrón", "employee" to "empleado", "promotion" to "ascenso",
                "contract" to "contrato", "unemployed" to "desempleado", "apply for a job" to "postularse a un empleo",
                "part-time" to "medio tiempo", "full-time" to "tiempo completo"
            )
        ),
        GuideVocabCategory(
            categoryName = "Viajar y culturas",
            level = "B1",
            items = listOf(
                "custom" to "costumbre", "tradition" to "tradición", "tourist" to "turista",
                "backpacking" to "viajar de mochilero", "accommodation" to "alojamiento/hospedaje", "foreign" to "extranjero",
                "souvenir" to "recuerdo/souvenir", "local" to "local/lugareño", "sightseeing" to "recorrido turístico",
                "culture shock" to "choque cultural"
            )
        ),
        GuideVocabCategory(
            categoryName = "Phrasal verbs comunes",
            level = "B1",
            items = listOf(
                "give up" to "rendirse / darse por vencido", "look for" to "buscar", "find out" to "descubrir / enterarse",
                "get on" to "llevarse bien / abordar", "turn off" to "apagar", "look after" to "cuidar de",
                "run out of" to "quedarse sin / agotarse", "put off" to "posponer / aplazar",
                "carry on" to "continuar / seguir adelante", "look forward to" to "esperar con ilusión / anhelar"
            )
        ),

        // ================= B2 VOCABULARY (10 TEMAS) =================
        GuideVocabCategory(
            categoryName = "Temas abstractos y opinión",
            level = "B2",
            items = listOf(
                "controversial" to "polémico / controvertido", "arguably" to "posiblemente / se puede argumentar",
                "drawback" to "desventaja / inconveniente", "benefit" to "beneficio / ventaja",
                "perspective" to "perspectiva", "debate" to "debate", "viewpoint" to "punto de vista",
                "to a certain extent" to "hasta cierto punto", "on the other hand" to "por otro lado",
                "outweigh" to "pesar más que / superar en importancia"
            )
        ),
        GuideVocabCategory(
            categoryName = "Trabajo y carrera profesional",
            level = "B2",
            items = listOf(
                "promotion" to "ascenso", "redundancy" to "despido por reestructuración", "deadline" to "fecha límite",
                "workload" to "carga de trabajo", "resign" to "renunciar", "applicant" to "solicitante / candidato",
                "qualification" to "acreditación / cualificación", "recruitment" to "contratación / reclutamiento",
                "appraisal" to "evaluación de desempeño", "colleague" to "colega / compañero de trabajo"
            )
        ),
        GuideVocabCategory(
            categoryName = "Medios de comunicación",
            level = "B2",
            items = listOf(
                "headline" to "titular de prensa", "broadcast" to "emisión / transmisión", "coverage" to "cobertura informativa",
                "censorship" to "censura", "subscription" to "suscripción", "influencer" to "creador influyente",
                "bias" to "sesgo / parcialidad", "misleading" to "engañoso", "journalist" to "periodista", "publish" to "publicar"
            )
        ),
        GuideVocabCategory(
            categoryName = "Ciencia y medio ambiente (avanzado)",
            level = "B2",
            items = listOf(
                "biodiversity" to "biodiversidad", "carbon footprint" to "huella de carbono", "ecosystem" to "ecosistema",
                "breakthrough" to "avance decisivo / gran hallazgo", "sustainable" to "sostenible",
                "endangered species" to "especies en peligro de extinción", "renewable" to "renovable",
                "conservation" to "conservación", "greenhouse effect" to "efecto invernadero"
            )
        ),
        GuideVocabCategory(
            categoryName = "Política y sociedad",
            level = "B2",
            items = listOf(
                "government" to "gobierno", "policy" to "política pública", "election" to "elección",
                "citizen" to "ciudadano/a", "law" to "ley", "rights" to "derechos",
                "protest" to "protesta", "inequality" to "desigualdad", "democracy" to "democracia", "campaign" to "campaña"
            )
        ),
        GuideVocabCategory(
            categoryName = "Salud y bienestar (avanzado)",
            level = "B2",
            items = listOf(
                "wellbeing" to "bienestar", "stress" to "estrés", "treatment" to "tratamiento",
                "diagnosis" to "diagnóstico", "recovery" to "recuperación", "therapy" to "terapia",
                "symptom" to "síntoma", "lifestyle" to "estilo de vida", "exhausted" to "exhausto / agotado",
                "cope with" to "afrontar / lidiar con"
            )
        ),
        GuideVocabCategory(
            categoryName = "Economía",
            level = "B2",
            items = listOf(
                "economy" to "economía", "inflation" to "inflación", "investment" to "inversión",
                "budget" to "presupuesto", "market" to "mercado", "consumer" to "consumidor",
                "income" to "ingresos", "debt" to "deuda", "afford" to "permitirse pagar / costear", "financial" to "financiero"
            )
        ),
        GuideVocabCategory(
            categoryName = "Expresiones idiomáticas",
            level = "B2",
            items = listOf(
                "break the ice" to "romper el hielo", "hit the nail on the head" to "dar en el clavo",
                "under the weather" to "enfermo/a / indispuesto/a", "once in a blue moon" to "muy de vez en cuando / rara vez",
                "cost an arm and a leg" to "costar un ojo de la cara / carísimo", "on the same page" to "de acuerdo / en la misma sintonía",
                "a piece of cake" to "pan comido / facilísimo"
            )
        ),
        GuideVocabCategory(
            categoryName = "Adjetivos avanzados de opinión",
            level = "B2",
            items = listOf(
                "fascinating" to "fascinante", "overwhelming" to "abrumador", "outrageous" to "indignante / inaudito",
                "remarkable" to "notable / extraordinario", "ambiguous" to "ambiguo", "essential" to "esencial",
                "reluctant" to "reacio / renuente", "controversial" to "controvertido", "inevitable" to "inevitable", "versatile" to "versátil"
            )
        ),
        GuideVocabCategory(
            categoryName = "Verbos y colocaciones avanzadas",
            level = "B2",
            items = listOf(
                "achieve a goal" to "alcanzar una meta", "make progress" to "progresar / avanzar",
                "take responsibility" to "asumir la responsabilidad", "raise awareness" to "concientizar / crear conciencia",
                "come to a conclusion" to "llegar a una conclusión", "tackle a problem" to "abordar / enfrentar un problema",
                "meet a deadline" to "cumplir con una fecha límite", "take part in" to "participar en",
                "deal with" to "tratar con / lidiar con", "come up with" to "idear / proponer"
            )
        )
    )
}

@Composable
fun CambridgeGuideScreen(
    viewModel: MainViewModel,
    onSpeak: (String, Boolean) -> Unit
) {
    var selectedLevelTab by remember { mutableStateOf("A1") } // "A1", "A2", "B1", "B2", "INFO", "EXAM_TYPES"
    var searchQuery by remember { mutableStateOf("") }
    var selectedSectionType by remember { mutableStateOf("ALL") } // "ALL", "GRAMMAR", "VOCAB"

    val userInputs = remember { mutableStateMapOf<String, String>() }
    val userEvaluations = remember { mutableStateMapOf<String, Boolean>() }
    val showAnswers = remember { mutableStateMapOf<String, Boolean>() }

    val levelTabs = listOf(
        "A1" to "Nivel A1",
        "A2" to "Nivel A2",
        "B1" to "Nivel B1",
        "B2" to "Nivel B2",
        "INFO" to "Exámenes Cambridge",
        "EXAM_TYPES" to "Tipos de Ejercicio"
    )

    Scaffold(
        topBar = {
            ScreenTopBar(
                title = "Guía Cambridge A1–B2",
                subtitle = "Gramática, Vocabulario y Ejercicios Oficiales",
                onBack = { viewModel.navigateToLearnSubScreen(LearnSubScreen.TOPICS_LIST) },
                actions = {
                    IconButton(
                        onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.CAMBRIDGE_SAMPLE_PAPERS) }
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = "Sample Papers Vol. 1 & 2", tint = BrandNavy)
                    }
                    IconButton(
                        onClick = { viewModel.navigateToLearnSubScreen(LearnSubScreen.INTEGRATED_SPACES) }
                    ) {
                        Icon(Icons.Default.Public, contentDescription = "Espacios Integrados en Línea", tint = BrandBlue)
                    }
                    IconButton(
                        onClick = {
                            userInputs.clear()
                            userEvaluations.clear()
                            showAnswers.clear()
                            searchQuery = ""
                        }
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reiniciar ejercicios", tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
            )
        }
    ) { innerPadding ->
        val query = searchQuery.trim().lowercase()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("cambridge_guide_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Search field
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar vocabulario o gramática (ej: parents, will, phrasal...)") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = BrandBlue) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Level Selector Tabs
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(levelTabs) { (tabKey, tabLabel) ->
                        val isSelected = selectedLevelTab == tabKey
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedLevelTab = tabKey },
                            label = {
                                Text(
                                    text = tabLabel,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandBlueLight,
                                selectedLabelColor = BrandBlue,
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier.testTag("guide_tab_$tabKey")
                        )
                    }
                }
            }

            // Sub-filtering (Gramática / Vocabulario) if in A1-B2
            if (selectedLevelTab in listOf("A1", "A2", "B1", "B2")) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("ALL" to "Todo el Nivel", "GRAMMAR" to "Solo Gramática", "VOCAB" to "Solo Vocabulario").forEach { (typeKey, typeLabel) ->
                            val isSelected = selectedSectionType == typeKey
                            ElevatedFilterChip(
                                selected = isSelected,
                                onClick = { selectedSectionType = typeKey },
                                label = { Text(typeLabel, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }

            // CEFR EXAM MAPPING VIEW
            if (selectedLevelTab == "INFO" && query.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.School, contentDescription = null, tint = BrandBlue)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "1. Niveles CEFR y Exámenes de Cambridge",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "El Marco Común Europeo de Referencia (MCER / CEFR) estandariza los niveles de dominio del idioma inglés. Cambridge English alinea sus certificaciones internacionales con estos niveles:",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            CambridgeGuideData.cefrExamMapping.forEach { (cefr, exam, note) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(BrandBlueLight.copy(alpha = 0.4f))
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = BrandBlue,
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(cefr, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(exam, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                        Text(note, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // EXAM TYPES VIEW
            if (selectedLevelTab == "EXAM_TYPES" && query.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Quiz, contentDescription = null, tint = SuccessGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "2. Tipos de Ejercicios Típicos de Cambridge",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            CambridgeGuideData.exerciseTypes.forEach { (name, desc) ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BrandBlue)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(desc, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // LEVEL CONTENT (A1, A2, B1, B2) OR GLOBAL SEARCH RESULTS
            if (selectedLevelTab in listOf("A1", "A2", "B1", "B2") || query.isNotEmpty()) {
                val currentLevel = selectedLevelTab

                // GRAMMAR SECTION
                if (selectedSectionType in listOf("ALL", "GRAMMAR") || query.isNotEmpty()) {
                    val levelGrammar = CambridgeGuideData.grammarTopics.filter { topic ->
                        val matchesLevel = (query.isNotEmpty()) || (topic.level == currentLevel)
                        val matchesQuery = query.isEmpty() ||
                                topic.title.lowercase().contains(query) ||
                                topic.explanation.lowercase().contains(query) ||
                                topic.code.lowercase().contains(query) ||
                                topic.examples.any { it.lowercase().contains(query) }
                        matchesLevel && matchesQuery
                    }

                    if (levelGrammar.isNotEmpty()) {
                        item {
                            Text(
                                text = if (query.isEmpty()) "Gramática Nivel $currentLevel" else "Gramática (${levelGrammar.size} resultados)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        items(levelGrammar) { topic ->
                            GuideGrammarCard(
                                topic = topic,
                                currentInput = userInputs[topic.id] ?: "",
                                onInputChange = { userInputs[topic.id] = it },
                                isCorrect = userEvaluations[topic.id],
                                showAnswer = showAnswers[topic.id] ?: false,
                                onCheckAnswer = {
                                    val input = (userInputs[topic.id] ?: "").trim().lowercase()
                                    val isRight = topic.acceptableAnswers.any { it.trim().lowercase() == input } ||
                                            topic.correctAnswer.lowercase().contains(input) && input.isNotEmpty()
                                    userEvaluations[topic.id] = isRight
                                },
                                onToggleAnswer = {
                                    showAnswers[topic.id] = !(showAnswers[topic.id] ?: false)
                                },
                                onSpeak = onSpeak
                            )
                        }
                    }
                }

                // VOCABULARY SECTION
                if (selectedSectionType in listOf("ALL", "VOCAB") || query.isNotEmpty()) {
                    val levelVocab = CambridgeGuideData.vocabCategories
                        .mapNotNull { category ->
                            val matchesLevel = (query.isNotEmpty()) || (category.level == currentLevel)
                            if (!matchesLevel) return@mapNotNull null

                            if (query.isEmpty()) {
                                category
                            } else {
                                val filteredItems = category.items.filter { (en, es) ->
                                    en.lowercase().contains(query) || es.lowercase().contains(query) || category.categoryName.lowercase().contains(query)
                                }
                                if (filteredItems.isNotEmpty()) category.copy(items = filteredItems) else null
                            }
                        }

                    if (levelVocab.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            val totalWords = levelVocab.sumOf { it.items.size }
                            Text(
                                text = if (query.isEmpty()) "Vocabulario Esencial Nivel $currentLevel (${levelVocab.size} categorías · $totalWords palabras)" else "Vocabulario ($totalWords palabras encontradas)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        items(levelVocab) { category ->
                            GuideVocabCategoryCard(
                                category = category,
                                onSpeak = onSpeak,
                                onSaveToSrs = { term, translation ->
                                    viewModel.saveWordToSrs(term, translation, "cambridge_${category.level}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GuideGrammarCard(
    topic: GuideGrammarTopic,
    currentInput: String,
    onInputChange: (String) -> Unit,
    isCorrect: Boolean?,
    showAnswer: Boolean,
    onCheckAnswer: () -> Unit,
    onToggleAnswer: () -> Unit,
    onSpeak: (String, Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Code and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = BrandBlue,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = topic.code,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = topic.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { onSpeak(topic.title, false) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = "Escuchar título", tint = BrandBlue, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Explanation
            Text(
                text = topic.explanation,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Examples
            Text(
                text = "Ejemplos:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = BrandBlue
            )
            topic.examples.forEach { example ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                ) {
                    Text("• ", color = BrandBlue, fontSize = 13.sp)
                    Text(
                        text = example,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { onSpeak(example, false) },
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Escuchar ejemplo", tint = BrandBlue, modifier = Modifier.size(14.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // Exercise Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = ModuleStatsPurple.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Ejercicio (${topic.exerciseType})",
                        color = ModuleStatsPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = topic.exercisePrompt,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Multiple choice options if present
            if (topic.exerciseOptions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                topic.exerciseOptions.chunked(2).forEach { rowOptions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowOptions.forEach { opt ->
                            val isSelected = currentInput.trim().lowercase() == opt.take(1).lowercase() || currentInput == opt
                            OutlinedButton(
                                onClick = {
                                    onInputChange(opt.take(1))
                                    onCheckAnswer()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) BrandBlueLight else Color.Transparent
                                )
                            ) {
                                Text(opt, fontSize = 12.sp, color = if (isSelected) BrandBlue else MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            } else {
                // Open cloze / Word formation / Transformation Input field
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = currentInput,
                        onValueChange = onInputChange,
                        placeholder = { Text("Escribe tu respuesta...", fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            onCheckAnswer()
                        })
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            onCheckAnswer()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text("Verificar", fontSize = 12.sp)
                    }
                }
            }

            // Feedback badge
            if (isCorrect != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isCorrect) SuccessGreen.copy(alpha = 0.15f) else BrandCoral.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = null,
                            tint = if (isCorrect) SuccessGreen else BrandCoral,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isCorrect) "¡Correcto!" else "Inténtalo de nuevo o revisa la respuesta.",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCorrect) SuccessGreen else BrandCoral
                        )
                    }
                }
            }

            // Answer reveal button
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onToggleAnswer,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = if (showAnswer) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (showAnswer) "Ocultar respuesta" else "Ver respuesta oficial",
                        fontSize = 12.sp
                    )
                }
            }

            if (showAnswer) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BrandBlueLight.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Key, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Respuesta: ${topic.correctAnswer}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = BrandBlue
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GuideVocabCategoryCard(
    category: GuideVocabCategory,
    onSpeak: (String, Boolean) -> Unit,
    onSaveToSrs: ((String, String) -> Unit)? = null
) {
    val savedTerms = remember { mutableStateMapOf<String, Boolean>() }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = SuccessGreen,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = category.level,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = category.categoryName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = BrandBlueLight.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${category.items.size} palabras",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandBlue,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Vocab chips / items grid
            category.items.chunked(2).forEach { pairRow ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pairRow.forEach { (en, es) ->
                        val isSaved = savedTerms[en] == true
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.background,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSpeak(en, false) }
                                .padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = en,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = BrandBlue
                                    )
                                    Text(
                                        text = es,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (onSaveToSrs != null) {
                                    IconButton(
                                        onClick = {
                                            onSaveToSrs(en, es)
                                            savedTerms[en] = true
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isSaved) Icons.Default.BookmarkAdded else Icons.Outlined.BookmarkAdd,
                                            contentDescription = if (isSaved) "Guardado en Repaso" else "Guardar en Repaso",
                                            tint = if (isSaved) SuccessGreen else BrandBlue.copy(alpha = 0.6f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Escuchar",
                                        tint = BrandBlue.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                    if (pairRow.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
