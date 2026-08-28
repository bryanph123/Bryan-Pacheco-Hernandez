package com.example.data.remote

import com.example.R

enum class YleExamVolume(val title: String, val badge: String, val description: String) {
    VOLUME_1("Sample Papers Vol. 1", "VOL 1", "Exámenes oficiales oficiales de muestra - Volumen 1"),
    VOLUME_2("Sample Papers Vol. 2", "VOL 2", "Exámenes oficiales actualizados - Volumen 2 (2024)")
}

enum class YleExamLevel(val title: String, val subtitle: String, val cefr: String, val bannerResId: Int) {
    STARTERS("Pre A1 Starters", "Young Learners Exam", "Pre A1", R.drawable.img_starters_banner_1787926659136),
    MOVERS("A1 Movers", "Young Learners Exam", "A1", R.drawable.img_movers_banner_1787926701970),
    FLYERS("A2 Flyers", "Young Learners Exam", "A2", R.drawable.img_flyers_banner_1787926754744)
}

enum class YleSkill(val title: String, val durationMinutes: Int, val questionsCount: Int) {
    LISTENING("Listening (Comprensión Auditiva)", 20, 20),
    READING_WRITING("Reading & Writing (Lectura y Escritura)", 20, 25),
    SPEAKING("Speaking (Expresión Oral)", 5, 4)
}

data class YleQuestionItem(
    val id: String,
    val partNumber: Int,
    val partTitle: String,
    val instructions: String,
    val questionText: String,
    val audioTranscript: String? = null,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val acceptedAlternatives: List<String> = emptyList(),
    val explanation: String,
    val contextStory: String? = null,
    val imageHintDesc: String? = null
)

object CambridgeSamplePapersData {

    // =========================================================================
    // ============================= VOLUME 1 ==================================
    // =========================================================================

    // --- PRE A1 STARTERS (VOL 1) ---
    val startersListeningV1 = listOf(
        YleQuestionItem(
            id = "v1_starters_l_p1_1",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people in the park",
            instructions = "Listen to the dialogue and match each name to their position in the park scene.",
            questionText = "Where is Lucy in the park?",
            audioTranscript = "F: There's a girl here, too. She's behind the pear tree!\nMch: Yes. She's funny. Her name's Lucy.\nF: And what's Lucy doing behind that tree?\nMch: Playing a game?",
            options = listOf("Behind the pear tree", "Feeding ducks", "With the kite", "Reading a book"),
            correctAnswer = "Behind the pear tree",
            explanation = "Lucy is hiding behind the pear tree."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p1_2",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people in the park",
            instructions = "Listen to the dialogue and match each name to their position.",
            questionText = "What is Jill doing by the pond?",
            audioTranscript = "Mch: And there's Jill. She's got some bread in her hand.\nF: Is she giving it to the ducks?\nMch: Yes! Jill loves ducks.",
            options = listOf("Girl feeding ducks", "Boy on bike", "Girl reading book", "Boy climbing tree"),
            correctAnswer = "Girl feeding ducks",
            explanation = "Jill is the girl feeding bread to the ducks."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p1_3",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people in the park",
            instructions = "Listen to the dialogue and match the person.",
            questionText = "Who is Dan?",
            audioTranscript = "F: That's a great kite!\nMch: Yes, that's Dan's kite.\nF: Is Dan the boy in the red T-shirt?\nMch: Yes, that's right.",
            options = listOf("Boy with the kite in red T-shirt", "Boy on bike", "Boy with cats", "Girl reading"),
            correctAnswer = "Boy with the kite in red T-shirt",
            explanation = "Dan is the boy in the red T-shirt flying a colorful kite."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p1_4",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people in the park",
            instructions = "Listen to the dialogue and match the person.",
            questionText = "What is Ann doing on the park bench?",
            audioTranscript = "F: One person is reading. What's her name?\nMch: The girl with the book?\nF: Yes.\nMch: That's my friend Ann. Reading is Ann's favourite hobby.",
            options = listOf("Girl reading a book on the bench", "Girl on bike", "Girl feeding ducks", "Girl with cats"),
            correctAnswer = "Girl reading a book on the bench",
            explanation = "Ann is reading her favorite book on the bench."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p1_5",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people in the park",
            instructions = "Listen to the dialogue and match the person.",
            questionText = "Who is Nick?",
            audioTranscript = "F: And what's that boy's name? The boy on the bike.\nMch: That's Nick. Nick's in my class at school.\nF: Oh! He's got a nice bike!\nMch: I know! It's new. He loves it.",
            options = listOf("Boy riding a new bike", "Boy flying kite", "Boy with cats", "Girl climbing tree"),
            correctAnswer = "Boy riding a new bike",
            explanation = "Nick is the boy riding his new bicycle."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p2_1",
            partNumber = 2,
            partTitle = "Part 2: Read the question. Write a name or number",
            instructions = "Listen and spell the family name.",
            questionText = "What is Kim's family name?",
            audioTranscript = "M: What's your family name, please?\nFch: It's Wall. W-A-L-L.\nM: Wall? That's my name, too!",
            correctAnswer = "Wall",
            acceptedAlternatives = listOf("WALL", "W-A-L-L", "wall"),
            explanation = "Kim's surname is Wall (spelled W-A-L-L)."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p2_2",
            partNumber = 2,
            partTitle = "Part 2: Read the question. Write a name or number",
            instructions = "Listen and write the street name.",
            questionText = "Where does Kim live? (in ... Street)",
            audioTranscript = "M: Where do you live, Kim?\nFch: In Sun Street.\nM: Sun Street?\nFch: Yes. S-U-N. It's behind the zoo.",
            correctAnswer = "Sun",
            acceptedAlternatives = listOf("SUN", "Sun Street", "S-U-N"),
            explanation = "Kim lives in Sun Street (S-U-N)."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p2_3",
            partNumber = 2,
            partTitle = "Part 2: Read the question. Write a name or number",
            instructions = "Listen and write the house number.",
            questionText = "What number is Kim's house?",
            audioTranscript = "M: What number's your house?\nFch: It's 15.\nM: 15. Oh, is it that house with the big garden?\nFch: Yes, it is. And it's got a pink door!",
            correctAnswer = "15",
            acceptedAlternatives = listOf("fifteen", "Fifteen", "FIFTEEN"),
            explanation = "Kim's house number is 15."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p2_4",
            partNumber = 2,
            partTitle = "Part 2: Read the question. Write a name or number",
            instructions = "Listen and write the horse's name.",
            questionText = "What is the name of Kim's horse?",
            audioTranscript = "M: What's your horse's name?\nFch: Tiger. That's T-I-G-E-R.\nM: Tiger?!\nFch: Yes, it's a funny name for a horse but I like it.",
            correctAnswer = "Tiger",
            acceptedAlternatives = listOf("TIGER", "T-I-G-E-R", "tiger"),
            explanation = "The horse is named Tiger (T-I-G-E-R)."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p2_5",
            partNumber = 2,
            partTitle = "Part 2: Read the question. Write a name or number",
            instructions = "Listen and write the horse's age.",
            questionText = "How old is Kim's horse?",
            audioTranscript = "M: How old is your horse?\nFch: He's seven.\nM: Seven?\nFch: Yes. And he can run and jump.",
            correctAnswer = "7",
            acceptedAlternatives = listOf("seven", "Seven", "SEVEN"),
            explanation = "Kim's horse is 7 years old."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p3_1",
            partNumber = 3,
            partTitle = "Part 3: Listen and tick the box",
            instructions = "Listen to the dialogue and choose the correct picture.",
            questionText = "Which picture are May and Sam looking at?",
            audioTranscript = "Fch: This picture's nice, Sam. Who's in it?\nMch: Mum, my grandpa and my cousin, Tom.\nFch: Where's your dad and your grandma?\nMch: They're not in this picture, May.",
            options = listOf("A (Mum, Dad, Sister)", "B (Mum, Grandpa, Cousin Tom)", "C (Grandma, Mum, Boy)"),
            correctAnswer = "B (Mum, Grandpa, Cousin Tom)",
            explanation = "Sam explains the picture contains Mum, his grandpa and his cousin Tom."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p3_2",
            partNumber = 3,
            partTitle = "Part 3: Listen and tick the box",
            instructions = "Listen to the dialogue and select what the class is doing.",
            questionText = "What are Mrs Good's class doing this afternoon?",
            audioTranscript = "M: Where are your class this afternoon, Mrs Good? At their swimming lesson?\nF: No, they're in the playground.\nM: Are they playing football?\nF: Not today. They're taking photos for our class book.",
            options = listOf("A (Taking photos for class book)", "B (Swimming in pool)", "C (Playing football)"),
            correctAnswer = "A (Taking photos for class book)",
            explanation = "Mrs Good explains the students are in the playground taking photos for their class book."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p3_3",
            partNumber = 3,
            partTitle = "Part 3: Listen and tick the box",
            instructions = "Listen to the dialogue and identify Mum's favorite fruit.",
            questionText = "What is Mum's favourite fruit?",
            audioTranscript = "Mch: Mum, can we have this coconut?\nF: Well, they're very nice but I can't open them.\nMch: What about these oranges?\nF: OK. They're my favourites. And let's have this pineapple too.",
            options = listOf("A (Oranges)", "B (Coconut)", "C (Pineapple)"),
            correctAnswer = "A (Oranges)",
            explanation = "Mum explicitly says about oranges: 'They're my favourites.'"
        ),
        YleQuestionItem(
            id = "v1_starters_l_p3_4",
            partNumber = 3,
            partTitle = "Part 3: Listen and tick the box",
            instructions = "Listen to Anna describing her dog.",
            questionText = "Which dog is Anna's?",
            audioTranscript = "Mch: Is that your dog, Anna?\nFch: No, my dog's dirty.\nMch: Is it young?\nFch: Yes. My brother's dog is that old one.",
            options = listOf("A (Clean white dog)", "B (Dirty young dog with spots)", "C (Old black dog)"),
            correctAnswer = "B (Dirty young dog with spots)",
            explanation = "Anna says her dog is dirty and young (Image B)."
        ),
        YleQuestionItem(
            id = "v1_starters_l_p3_5",
            partNumber = 3,
            partTitle = "Part 3: Listen and tick the box",
            instructions = "Listen to what Lucy is wearing.",
            questionText = "What is Lucy wearing?",
            audioTranscript = "F: Lucy, your skirt's on the bed.\nFch: Thanks, Mum but I don't want it. I'm wearing my jeans.\nF: And your new T-shirt?\nFch: Yes. It's great!",
            options = listOf("A (Blue jacket and trousers)", "B (Skirt and short sleeves)", "C (Jeans and new blue T-shirt)"),
            correctAnswer = "C (Jeans and new blue T-shirt)",
            explanation = "Lucy is wearing her jeans and her new T-shirt."
        )
    )

    val startersReadingWritingV1 = listOf(
        YleQuestionItem(
            id = "v1_starters_rw_p1_1",
            partNumber = 1,
            partTitle = "Part 1: Look and read. Put a tick (✔) or a cross (✘)",
            instructions = "Read the statement about the picture and decide if it is correct or false.",
            questionText = "Picture shows a motorcycle. Statement: 'This is a helicopter.'",
            options = listOf("✔ (True)", "✘ (False)"),
            correctAnswer = "✘ (False)",
            explanation = "It is a motorbike, not a helicopter."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p1_2",
            partNumber = 1,
            partTitle = "Part 1: Look and read. Put a tick (✔) or a cross (✘)",
            instructions = "Read the statement about the antique clock.",
            questionText = "Picture shows a wall clock. Statement: 'This is a clock.'",
            options = listOf("✔ (True)", "✘ (False)"),
            correctAnswer = "✔ (True)",
            explanation = "The picture clearly displays a clock."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p1_3",
            partNumber = 1,
            partTitle = "Part 1: Look and read. Put a tick (✔) or a cross (✘)",
            instructions = "Read the statement about seashells on the sand.",
            questionText = "Picture shows seashells by the sea. Statement: 'These are shells.'",
            options = listOf("✔ (True)", "✘ (False)"),
            correctAnswer = "✔ (True)",
            explanation = "These are indeed seashells."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p1_4",
            partNumber = 1,
            partTitle = "Part 1: Look and read. Put a tick (✔) or a cross (✘)",
            instructions = "Read the statement about shoes.",
            questionText = "Picture shows a pair of shoes. Statement: 'This is a sock.'",
            options = listOf("✔ (True)", "✘ (False)"),
            correctAnswer = "✘ (False)",
            explanation = "They are shoes, not socks."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p1_5",
            partNumber = 1,
            partTitle = "Part 1: Look and read. Put a tick (✔) or a cross (✘)",
            instructions = "Read the statement about chairs in a room.",
            questionText = "Picture shows three colored chairs. Statement: 'These are chairs.'",
            options = listOf("✔ (True)", "✘ (False)"),
            correctAnswer = "✔ (True)",
            explanation = "These are three chairs."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p2_1",
            partNumber = 2,
            partTitle = "Part 2: Look and read. Write yes or no",
            instructions = "Look at the living room picture (man playing guitar, children dancing/reading).",
            questionText = "1. The man has got black hair and glasses.",
            options = listOf("yes", "no"),
            correctAnswer = "yes",
            explanation = "The man playing the guitar has black hair and is wearing glasses."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p2_2",
            partNumber = 2,
            partTitle = "Part 2: Look and read. Write yes or no",
            instructions = "Look at the bookcase and lamp.",
            questionText = "2. There is a lamp on the bookcase.",
            options = listOf("yes", "no"),
            correctAnswer = "yes",
            explanation = "A yellow lamp is resting on top of the bookcase."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p2_3",
            partNumber = 2,
            partTitle = "Part 2: Look and read. Write yes or no",
            instructions = "Look at the children in the center of the room.",
            questionText = "3. Some of the children are singing.",
            options = listOf("yes", "no"),
            correctAnswer = "no",
            explanation = "The children are dancing and one girl is sitting reading; none are singing into a microphone."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p2_4",
            partNumber = 2,
            partTitle = "Part 2: Look and read. Write yes or no",
            instructions = "Look at the woman standing on the right.",
            questionText = "4. The woman is holding some drinks.",
            options = listOf("yes", "no"),
            correctAnswer = "yes",
            explanation = "The mother is carrying two glasses with orange drinks."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p2_5",
            partNumber = 2,
            partTitle = "Part 2: Look and read. Write yes or no",
            instructions = "Look under the armchair.",
            questionText = "5. The cat is sleeping under an armchair.",
            options = listOf("yes", "no"),
            correctAnswer = "yes",
            explanation = "The cat is fast asleep under the armchair."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p3_1",
            partNumber = 3,
            partTitle = "Part 3: Unscramble letters into animal words",
            instructions = "Unscramble the letters: k - c - u - d",
            questionText = "Picture of a green-headed duck: _ _ _ _",
            correctAnswer = "duck",
            acceptedAlternatives = listOf("DUCK", "Duck"),
            explanation = "d - u - c - k = duck."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p3_2",
            partNumber = 3,
            partTitle = "Part 3: Unscramble letters into animal words",
            instructions = "Unscramble the letters: e - u - m - s - o",
            questionText = "Picture of a small brown mouse: _ _ _ _ _",
            correctAnswer = "mouse",
            acceptedAlternatives = listOf("MOUSE", "Mouse"),
            explanation = "m - o - u - s - e = mouse."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p3_3",
            partNumber = 3,
            partTitle = "Part 3: Unscramble letters into animal words",
            instructions = "Unscramble the letters: h - p - i - p - o",
            questionText = "Picture of a large grey hippopotamus: _ _ _ _ _",
            correctAnswer = "hippo",
            acceptedAlternatives = listOf("HIPPO", "Hippo"),
            explanation = "h - i - p - p - o = hippo."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p3_4",
            partNumber = 3,
            partTitle = "Part 3: Unscramble letters into animal words",
            instructions = "Unscramble the letters: o - n - m - y - k - e",
            questionText = "Picture of a cheeky monkey: _ _ _ _ _ _",
            correctAnswer = "monkey",
            acceptedAlternatives = listOf("MONKEY", "Monkey"),
            explanation = "m - o - n - k - e - y = monkey."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p3_5",
            partNumber = 3,
            partTitle = "Part 3: Unscramble letters into animal words",
            instructions = "Unscramble the letters: c - h - i - c - k - e - n",
            questionText = "Picture of a rooster/chicken: _ _ _ _ _ _ _",
            correctAnswer = "chicken",
            acceptedAlternatives = listOf("CHICKEN", "Chicken"),
            explanation = "c - h - i - c - k - e - n = chicken."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p4_1",
            partNumber = 4,
            partTitle = "Part 4: Read and fill the blanks (Lizards)",
            instructions = "Word bank: [animals, tail, balloon, trees, legs, spiders, teacher, sand]",
            questionText = "Many lizards are green, grey or yellow. Some like eating (1) _______ and some like eating fruit.",
            options = listOf("spiders", "tail", "trees", "legs", "sand"),
            correctAnswer = "spiders",
            explanation = "Lizards eat insects and spiders."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p4_2",
            partNumber = 4,
            partTitle = "Part 4: Read and fill the blanks (Lizards)",
            instructions = "Word bank: [animals, tail, balloon, trees, legs, spiders, teacher, sand]",
            questionText = "A lizard can run on its four (2) _______",
            options = listOf("legs", "tail", "trees", "sand", "spiders"),
            correctAnswer = "legs",
            explanation = "A lizard has four legs to run."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p4_3",
            partNumber = 4,
            partTitle = "Part 4: Read and fill the blanks (Lizards)",
            instructions = "Word bank: [animals, tail, balloon, trees, legs, spiders, teacher, sand]",
            questionText = "and it has a long (3) _______ at the end of its body.",
            options = listOf("tail", "legs", "trees", "sand", "spiders"),
            correctAnswer = "tail",
            explanation = "A lizard has a long tail at the end of its body."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p4_4",
            partNumber = 4,
            partTitle = "Part 4: Read and fill the blanks (Lizards)",
            instructions = "Word bank: [animals, tail, balloon, trees, legs, spiders, teacher, sand]",
            questionText = "Many lizards live in (4) _______",
            options = listOf("trees", "tail", "legs", "sand", "spiders"),
            correctAnswer = "trees",
            explanation = "Lizards often dwell and climb in trees."
        ),
        YleQuestionItem(
            id = "v1_starters_rw_p4_5",
            partNumber = 4,
            partTitle = "Part 4: Read and fill the blanks (Lizards)",
            instructions = "Word bank: [animals, tail, balloon, trees, legs, spiders, teacher, sand]",
            questionText = "but, at the beach, you can find some lizards on the (5) _______ . Lizards love sleeping in the sun!",
            options = listOf("sand", "trees", "legs", "tail", "spiders"),
            correctAnswer = "sand",
            explanation = "At the beach, lizards sunbathe on the warm sand."
        )
    )

    // --- A1 MOVERS (VOL 1) ---
    val moversListeningV1 = listOf(
        YleQuestionItem(
            id = "v1_movers_l_p1_1",
            partNumber = 1,
            partTitle = "Part 1: Listen and identify people at the garden birthday party",
            instructions = "Listen to Sally and Grandpa describing the party.",
            questionText = "Who is Nick?",
            audioTranscript = "M: That boy's carrying the birthday cake. It's very big.\nFch: That's Nick.\nM: He's not walking very carefully with it.\nFch: I know. Oh dear!",
            options = listOf("Boy carrying big birthday cake", "Boy sitting on mat with toy truck", "Man with hat carrying sandwiches", "Woman cleaning table"),
            correctAnswer = "Boy carrying big birthday cake",
            explanation = "Nick is the boy carefully carrying the big birthday cake."
        ),
        YleQuestionItem(
            id = "v1_movers_l_p1_2",
            partNumber = 1,
            partTitle = "Part 1: Listen and identify people",
            instructions = "Listen to the description of the boy on the mat.",
            questionText = "Who is Ben?",
            audioTranscript = "M: He's sitting on the mat.\nFch: Oh, yes. And he's playing with a toy truck.\nM: That's right.\nFch: That boy's name's Ben. He's Sally's cousin.",
            options = listOf("Boy sitting on mat playing with toy truck", "Boy with cake", "Man with hat", "Woman in tree"),
            correctAnswer = "Boy sitting on mat playing with toy truck",
            explanation = "Ben is Sally's cousin, sitting on the mat playing with a toy truck."
        ),
        YleQuestionItem(
            id = "v1_movers_l_p2_1",
            partNumber = 2,
            partTitle = "Part 2: Listen and write details about the Zoo trip",
            instructions = "Listen and complete the zoo name.",
            questionText = "Name of zoo: Jungle _______",
            audioTranscript = "Mch: Is the zoo called 'Jungle' something?\nF: That's right. It's Jungle Hill.\nMch: Jungle what?\nF: Hill. That's H-I-double L.",
            correctAnswer = "Hill",
            acceptedAlternatives = listOf("HILL", "H-I-L-L", "hill"),
            explanation = "The zoo is named Jungle Hill."
        ),
        YleQuestionItem(
            id = "v1_movers_l_p2_2",
            partNumber = 2,
            partTitle = "Part 2: Listen and write details about the Zoo trip",
            instructions = "Listen and write the number of animal kinds.",
            questionText = "Number of different kinds of animals:",
            audioTranscript = "F: It says in this book there are 89 different kinds of animals.\nMch: 89?\nF: Yes.",
            correctAnswer = "89",
            acceptedAlternatives = listOf("eighty-nine", "Eighty-nine", "eighty nine", "89 kinds"),
            explanation = "There are 89 different kinds of animals in the zoo."
        ),
        YleQuestionItem(
            id = "v1_movers_l_p2_3",
            partNumber = 2,
            partTitle = "Part 2: Listen and write details about the Zoo trip",
            instructions = "Listen and write which animals can be fed.",
            questionText = "Can give food to:",
            audioTranscript = "Mch: Can we give food to them?\nF: No, we can't. But we can give food to the parrots.\nMch: Great. I love parrots.",
            correctAnswer = "parrots",
            acceptedAlternatives = listOf("the parrots", "parrot", "Parrots", "PARROTS"),
            explanation = "Visitors are allowed to feed the parrots."
        ),
        YleQuestionItem(
            id = "v1_movers_l_p2_4",
            partNumber = 2,
            partTitle = "Part 2: Listen and write details about the Zoo trip",
            instructions = "Listen and write where the animal food store is located.",
            questionText = "Animal food in store next to:",
            audioTranscript = "Mch: Where can we buy food for them?\nF: At the zoo store.\nMch: Where's that?\nF: Next to the café.",
            correctAnswer = "café",
            acceptedAlternatives = listOf("cafe", "the café", "the cafe", "Cafe"),
            explanation = "The animal food is sold at the store next to the café."
        ),
        YleQuestionItem(
            id = "v1_movers_l_p2_5",
            partNumber = 2,
            partTitle = "Part 2: Listen and write details about the Zoo trip",
            instructions = "Listen and write what food will be eaten on the train.",
            questionText = "Food on train: _______ and lemonade",
            audioTranscript = "F: On the train, we can buy burgers and lemonade.\nMch: Burgers and lemonade. Great! My favourites.",
            correctAnswer = "burgers",
            acceptedAlternatives = listOf("hamburger", "hamburgers", "burger", "Burgers"),
            explanation = "They will eat burgers and lemonade on the train."
        ),
        YleQuestionItem(
            id = "v1_movers_l_p4_1",
            partNumber = 4,
            partTitle = "Part 4: Listen and tick the box",
            instructions = "Listen to Vicky's piano teacher description.",
            questionText = "Who is Vicky's piano teacher?",
            audioTranscript = "Fch: Oh, look. There's my piano teacher.\nMch: The man with the moustache?\nFch: Yes. And he's very thin.",
            options = listOf("A (Fat man with moustache in light suit)", "B (Thin man with moustache in red jacket and bow tie)", "C (Young man with green jacket, no moustache)"),
            correctAnswer = "B (Thin man with moustache in red jacket and bow tie)",
            explanation = "The teacher is the thin man with a moustache (Picture B)."
        ),
        YleQuestionItem(
            id = "v1_movers_l_p4_3",
            partNumber = 4,
            partTitle = "Part 4: Listen and tick the box",
            instructions = "Listen to where Peter found the shell.",
            questionText = "Where did Peter find the shell?",
            audioTranscript = "Mch: I found this one in a different place. In a big forest. Isn't that great?\nF: Wow, I am surprised.",
            options = listOf("A (In a big forest)", "B (Near waterfall)", "C (On the beach)"),
            correctAnswer = "A (In a big forest)",
            explanation = "Peter surprisingly found the seashell in a big forest (Picture A)."
        )
    )

    val moversReadingWritingV1 = listOf(
        YleQuestionItem(
            id = "v1_movers_rw_p1_1",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [an island, a sandwich, a driver, a band, tea, a city, a field, a nurse]",
            questionText = "1. This person helps people who aren't well in hospital.",
            options = listOf("a nurse", "a driver", "a band", "a city"),
            correctAnswer = "a nurse",
            explanation = "A nurse works in a hospital caring for patients."
        ),
        YleQuestionItem(
            id = "v1_movers_rw_p1_2",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [an island, a sandwich, a driver, a band, tea, a city, a field, a nurse]",
            questionText = "2. Some people put milk or lemon in this drink.",
            options = listOf("tea", "a sandwich", "a field", "an island"),
            correctAnswer = "tea",
            explanation = "Tea is a drink commonly enjoyed with milk or lemon."
        ),
        YleQuestionItem(
            id = "v1_movers_rw_p1_3",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [an island, a sandwich, a driver, a band, tea, a city, a field, a nurse]",
            questionText = "3. There are lots of cars, buses and people in this busy place.",
            options = listOf("a city", "a field", "an island", "a band"),
            correctAnswer = "a city",
            explanation = "A city is busy with transportation and crowds."
        ),
        YleQuestionItem(
            id = "v1_movers_rw_p1_4",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [an island, a sandwich, a driver, a band, tea, a city, a field, a nurse]",
            questionText = "4. You can put cheese or meat between bread to make this.",
            options = listOf("a sandwich", "tea", "a field", "a nurse"),
            correctAnswer = "a sandwich",
            explanation = "A sandwich is made of cheese/meat between bread."
        ),
        YleQuestionItem(
            id = "v1_movers_rw_p1_5",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [an island, a sandwich, a driver, a band, tea, a city, a field, a nurse]",
            questionText = "5. This is part of a farm where you often see vegetable plants.",
            options = listOf("a field", "a city", "an island", "a driver"),
            correctAnswer = "a field",
            explanation = "A field is an open agricultural plot on a farm."
        ),
        YleQuestionItem(
            id = "v1_movers_rw_p4_1",
            partNumber = 4,
            partTitle = "Part 4: Choose the right word (Dolphins)",
            instructions = "Read and choose the best grammatical word.",
            questionText = "Dolphins are smaller _______ most whales and they have small teeth.",
            options = listOf("than", "then", "that"),
            correctAnswer = "than",
            explanation = "Comparative adjective 'smaller' takes 'than'."
        ),
        YleQuestionItem(
            id = "v1_movers_rw_p4_2",
            partNumber = 4,
            partTitle = "Part 4: Choose the right word (Dolphins)",
            instructions = "Choose the adverb of manner.",
            questionText = "Dolphins are very clever animals. They learn things very _______",
            options = listOf("quickly", "quick", "quickest"),
            correctAnswer = "quickly",
            explanation = "'Quickly' modifies the verb 'learn'."
        ),
        YleQuestionItem(
            id = "v1_movers_rw_p4_3",
            partNumber = 4,
            partTitle = "Part 4: Choose the right word (Dolphins)",
            instructions = "Choose the preposition.",
            questionText = "They like to play in the water and to jump _______ of the water and back in again.",
            options = listOf("out", "from", "up"),
            correctAnswer = "out",
            explanation = "Jump 'out of' the water."
        ),
        YleQuestionItem(
            id = "v1_movers_rw_p4_4",
            partNumber = 4,
            partTitle = "Part 4: Choose the right word (Dolphins)",
            instructions = "Choose the relative pronoun for people.",
            questionText = "A lot of people _______ sail boats say that dolphins like to be near people.",
            options = listOf("who", "which", "what"),
            correctAnswer = "who",
            explanation = "'Who' refers to people who sail boats."
        ),
        YleQuestionItem(
            id = "v1_movers_rw_p4_5",
            partNumber = 4,
            partTitle = "Part 4: Choose the right word (Dolphins)",
            instructions = "Choose the present simple verb.",
            questionText = "They come very near to boats and sometimes they _______ with the boats for days.",
            options = listOf("swim", "swam", "swimming"),
            correctAnswer = "swim",
            explanation = "Present simple plural subject 'they swim'."
        )
    )

    // --- A2 FLYERS (VOL 1) ---
    val flyersListeningV1 = listOf(
        YleQuestionItem(
            id = "v1_flyers_l_p1_1",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people at the lake",
            instructions = "Listen to the lake scene recording.",
            questionText = "Who is Richard?",
            audioTranscript = "Fch: Look at that boy!\nF: The one with the striped sweater?\nFch: Yes. That's Richard. His dad teaches me geography.\nF: At your school?\nFch: Yes. I think he's drinking lemonade.",
            options = listOf("Boy in striped sweater drinking lemonade", "Girl laughing on red bike", "Boy with red belt and toy helicopter", "Woman feeding swans"),
            correctAnswer = "Boy in striped sweater drinking lemonade",
            explanation = "Richard is the boy in the striped sweater holding a bottle."
        ),
        YleQuestionItem(
            id = "v1_flyers_l_p1_2",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people at the lake",
            instructions = "Listen to the conversation about the girl on the bike.",
            questionText = "Who is Sally?",
            audioTranscript = "F: Who's that girl on the bike?\nFch: The one with the short blonde hair?\nF: No, not her. The one who's laughing.\nFch: Oh, that's Sally. She's my best friend.",
            options = listOf("Girl laughing while riding her bike", "Girl in striped sweater", "Woman with puppy", "Boy with toy helicopter"),
            correctAnswer = "Girl laughing while riding her bike",
            explanation = "Sally is the girl laughing on the bike."
        ),
        YleQuestionItem(
            id = "v1_flyers_l_p2_1",
            partNumber = 2,
            partTitle = "Part 2: Listen and write details about The Space Club",
            instructions = "Listen to details about what children learn in the club.",
            questionText = "Children learn about: the _______",
            audioTranscript = "Mch: What do people do at this club?\nM: Well, they can learn about the stars, and they can make new friends, too.",
            correctAnswer = "stars",
            acceptedAlternatives = listOf("the stars", "Stars", "STARS"),
            explanation = "Children learn about the stars."
        ),
        YleQuestionItem(
            id = "v1_flyers_l_p2_2",
            partNumber = 2,
            partTitle = "Part 2: Listen and write details about The Space Club",
            instructions = "Listen to what they observe when sky is clear.",
            questionText = "Sometimes they look at: the _______",
            audioTranscript = "M: And sometimes they go out when it's dark. They look at the moon when there are no clouds in the sky.",
            correctAnswer = "moon",
            acceptedAlternatives = listOf("the moon", "Moon", "MOON"),
            explanation = "They look at the moon."
        ),
        YleQuestionItem(
            id = "v1_flyers_l_p2_3",
            partNumber = 2,
            partTitle = "Part 2: Listen and write details about The Space Club",
            instructions = "Listen to what equipment children should bring.",
            questionText = "Children should bring: a _______",
            audioTranscript = "M: No, but it's a good idea to bring a torch. It'll be dark outside and they must be careful when they go out.",
            correctAnswer = "torch",
            acceptedAlternatives = listOf("flashlight", "a torch", "Torch", "TORCH"),
            explanation = "Children are advised to bring a torch (flashlight)."
        ),
        YleQuestionItem(
            id = "v1_flyers_l_p2_4",
            partNumber = 2,
            partTitle = "Part 2: Listen and write details about The Space Club",
            instructions = "Listen to what they watch on cloudy nights.",
            questionText = "If it's cloudy, children watch: _______",
            audioTranscript = "M: No Michael. But the club has some very interesting DVDs. So we watch those on nights like that.",
            correctAnswer = "DVDs",
            acceptedAlternatives = listOf("DVD", "dvds", "films", "interesting DVDs"),
            explanation = "They watch DVDs when the sky is cloudy."
        ),
        YleQuestionItem(
            id = "v1_flyers_l_p2_5",
            partNumber = 2,
            partTitle = "Part 2: Listen and write details about The Space Club",
            instructions = "Listen and spell the guest speaker's surname.",
            questionText = "Person who sometimes talks to club: Mr _______",
            audioTranscript = "M: And a friend of mine from the university sometimes comes to talk about space in the future. His name's Mr Bailey.\nMch: How do you spell his surname?\nM: B-A-I-L-E-Y.",
            correctAnswer = "Bailey",
            acceptedAlternatives = listOf("BAILEY", "B-A-I-L-E-Y", "bailey"),
            explanation = "Mr Bailey (spelled B-A-I-L-E-Y)."
        )
    )

    val flyersReadingWritingV1 = listOf(
        YleQuestionItem(
            id = "v1_flyers_rw_p1_1",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [an astronaut, a pilot, golf, sugar, hockey, magazines, baseball, postcards, salt, basketball, jam, stamps, a journalist, letters, a photographer]",
            questionText = "1. This is made from fruit and you can put it on your bread with a knife.",
            options = listOf("jam", "sugar", "salt", "magazines"),
            correctAnswer = "jam",
            explanation = "Jam is made from fruit and spread on bread."
        ),
        YleQuestionItem(
            id = "v1_flyers_rw_p1_2",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [an astronaut, a pilot, golf, sugar, hockey, magazines, baseball, postcards, salt, basketball, jam, stamps, a journalist, letters, a photographer]",
            questionText = "2. Players in this game throw, catch and hit the ball on a sports field.",
            options = listOf("baseball", "basketball", "golf", "hockey"),
            correctAnswer = "baseball",
            explanation = "Baseball involves throwing, catching and hitting with a bat."
        ),
        YleQuestionItem(
            id = "v1_flyers_rw_p1_3",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [an astronaut, a pilot, golf, sugar, hockey, magazines, baseball, postcards, salt, basketball, jam, stamps, a journalist, letters, a photographer]",
            questionText = "3. These have pictures on them and you can write on the back and send them to friends when you're on holiday.",
            options = listOf("postcards", "letters", "magazines", "stamps"),
            correctAnswer = "postcards",
            explanation = "Postcards have pictures on the front and space for messages on the back."
        ),
        YleQuestionItem(
            id = "v1_flyers_rw_p1_4",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [an astronaut, a pilot, golf, sugar, hockey, magazines, baseball, postcards, salt, basketball, jam, stamps, a journalist, letters, a photographer]",
            questionText = "4. It is this person's job to write about news in a newspaper.",
            options = listOf("a journalist", "a pilot", "a photographer", "an astronaut"),
            correctAnswer = "a journalist",
            explanation = "A journalist reports and writes news articles."
        ),
        YleQuestionItem(
            id = "v1_flyers_rw_p1_5",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [an astronaut, a pilot, golf, sugar, hockey, magazines, baseball, postcards, salt, basketball, jam, stamps, a journalist, letters, a photographer]",
            questionText = "5. You buy these and put them on your envelopes before you post them.",
            options = listOf("stamps", "postcards", "letters", "magazines"),
            correctAnswer = "stamps",
            explanation = "Postage stamps are attached to envelopes."
        )
    )

    // =========================================================================
    // ============================= VOLUME 2 ==================================
    // =========================================================================

    // --- PRE A1 STARTERS (VOL 2) ---
    val startersListeningV2 = listOf(
        YleQuestionItem(
            id = "v2_starters_l_p1_1",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people in the park with tennis & tablet",
            instructions = "Listen to the park recording and match names to people.",
            questionText = "Who is Grace?",
            audioTranscript = "M: Who's that? The girl with the red tennis racket?\nF: That's Grace, I like her long hair.\nM: Yes. I like Grace's hair too.",
            options = listOf("Girl with red tennis racket", "Girl with tablet on chair", "Boy in grey shorts with bike", "Boy holding bread for birds"),
            correctAnswer = "Girl with red tennis racket",
            explanation = "Grace is the girl with the red tennis racket and long hair."
        ),
        YleQuestionItem(
            id = "v2_starters_l_p1_2",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people in the park",
            instructions = "Listen to the description of the girl on the chair.",
            questionText = "Who is Alice?",
            audioTranscript = "M: And can you see Alice?\nF: Sorry? Alice?\nM: Yes. She's the girl with the tablet.\nF: Oh! The girl on the chair?\nM: Yes, she's playing a game on it.",
            options = listOf("Girl on the chair with tablet", "Girl with tennis racket", "Girl running in pink shirt", "Boy with bike"),
            correctAnswer = "Girl on the chair with tablet",
            explanation = "Alice is sitting on a chair playing a game on her tablet."
        ),
        YleQuestionItem(
            id = "v2_starters_l_p1_3",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people in the park",
            instructions = "Listen to the description of Bill.",
            questionText = "Who is Bill?",
            audioTranscript = "F: What's that boy's name?\nM: The boy in the grey shorts? That's Bill.\nF: And is that Bill's bike?\nM: Yes. It's cool.",
            options = listOf("Boy in grey shorts next to bike", "Boy flying kite", "Boy holding bread", "Girl with tennis racket"),
            correctAnswer = "Boy in grey shorts next to bike",
            explanation = "Bill is the boy in grey shorts standing near his bicycle."
        ),
        YleQuestionItem(
            id = "v2_starters_l_p1_4",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people in the park",
            instructions = "Listen to who is holding bread for the birds.",
            questionText = "Who is Matt?",
            audioTranscript = "F: Who's that boy? He's holding some bread.\nM: That's Matt. The bread is for the birds.\nF: Does Matt like coming to the park?\nM: Yes. He loves it.",
            options = listOf("Boy holding bread for the birds", "Boy with bike", "Boy playing tennis", "Girl on chair"),
            correctAnswer = "Boy holding bread for the birds",
            explanation = "Matt is holding bread to feed the birds."
        ),
        YleQuestionItem(
            id = "v2_starters_l_p1_5",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people in the park",
            instructions = "Listen to who is running.",
            questionText = "Who is Kim?",
            audioTranscript = "F: And who's that girl? She's running ...\nM: The girl in the pink T-shirt? That's Kim.\nF: Oh.\nM: Kim likes playing here too.",
            options = listOf("Girl in pink T-shirt running", "Girl on chair", "Girl with tennis racket", "Boy with bread"),
            correctAnswer = "Girl in pink T-shirt running",
            explanation = "Kim is running in a pink T-shirt."
        ),

        // Part 2: Pat and her birthday details
        YleQuestionItem(
            id = "v2_starters_l_p2_1",
            partNumber = 2,
            partTitle = "Part 2: Read the questions. Write a name or a number",
            instructions = "Listen and write how many cousins Pat has.",
            questionText = "1. How many cousins has Pat got?",
            audioTranscript = "M: How many cousins has she got?\nFch: She's got twelve.\nM: Pardon! How many?\nFch: Twelve.",
            correctAnswer = "12",
            acceptedAlternatives = listOf("twelve", "Twelve", "TWELVE"),
            explanation = "Pat has 12 cousins."
        ),
        YleQuestionItem(
            id = "v2_starters_l_p2_2",
            partNumber = 2,
            partTitle = "Part 2: Read the questions. Write a name or a number",
            instructions = "Listen and write the name of Pat's favourite game.",
            questionText = "2. What is the name of Pat's favourite game?",
            audioTranscript = "M: What's her favourite game?\nFch: The name of her favourite game is 'DUCK'!\nM: Duck? Do you spell that D-U-C-K?\nFch: Yes, it's a very funny game.",
            correctAnswer = "DUCK",
            acceptedAlternatives = listOf("Duck", "duck", "D-U-C-K"),
            explanation = "Pat's favourite game is DUCK (D-U-C-K)."
        ),
        YleQuestionItem(
            id = "v2_starters_l_p2_3",
            partNumber = 2,
            partTitle = "Part 2: Read the questions. Write a name or a number",
            instructions = "Listen and write the beach name.",
            questionText = "3. Which beach does Pat like to go to? (....... Beach)",
            audioTranscript = "M: And where does Pat like going?\nFch: She likes going to the beach.\nM: Which one?\nFch: She likes going to Shell Beach. You spell that S-H-E-L-L.",
            correctAnswer = "Shell",
            acceptedAlternatives = listOf("SHELL", "shell", "S-H-E-L-L", "Shell Beach"),
            explanation = "Pat likes going to Shell Beach (S-H-E-L-L)."
        ),
        YleQuestionItem(
            id = "v2_starters_l_p2_4",
            partNumber = 2,
            partTitle = "Part 2: Read the questions. Write a name or a number",
            instructions = "Listen and write how many books Pat has.",
            questionText = "4. How many books has Pat got?",
            audioTranscript = "Fch: Pat likes reading, too.\nM: Does she? How many books has she got?\nFch: She's got twenty books in her cupboard.\nM: Twenty! Wow!",
            correctAnswer = "20",
            acceptedAlternatives = listOf("twenty", "Twenty", "TWENTY"),
            explanation = "Pat has 20 books in her cupboard."
        ),
        YleQuestionItem(
            id = "v2_starters_l_p2_5",
            partNumber = 2,
            partTitle = "Part 2: Read the questions. Write a name or a number",
            instructions = "Listen and write the teacher's name.",
            questionText = "5. What is the teacher's name? (Mr. .......)",
            audioTranscript = "M: What's the teacher's name?\nFch: His name is Mr Lorry. You spell that L-O-R-R-Y.\nM: Mr Lorry? What a funny name!",
            correctAnswer = "Lorry",
            acceptedAlternatives = listOf("LORRY", "lorry", "L-O-R-R-Y", "Mr Lorry"),
            explanation = "The teacher is Mr Lorry (L-O-R-R-Y)."
        ),

        // Part 3: Multiple choice tick
        YleQuestionItem(
            id = "v2_starters_l_p3_1",
            partNumber = 3,
            partTitle = "Part 3: Listen and tick the box",
            instructions = "Listen to who Dad is talking to on the phone.",
            questionText = "1. Who is Dad talking to on the phone?",
            audioTranscript = "Mch: Who's Dad talking to, Mum?\nF: He's phoning Grandpa about dinner.\nMch: Can Alex, my friend from school, have dinner with us?\nF: Not today.",
            options = listOf("A (Boy friend)", "B (Grandpa with grey hair & moustache)", "C (Girl with blonde hair)"),
            correctAnswer = "B (Grandpa with grey hair & moustache)",
            explanation = "Dad is talking to Grandpa on the phone."
        ),
        YleQuestionItem(
            id = "v2_starters_l_p3_2",
            partNumber = 3,
            partTitle = "Part 3: Listen and tick the box",
            instructions = "Listen to what the crocodile is doing.",
            questionText = "2. What is the crocodile doing?",
            audioTranscript = "Fch: I love the crocodile in this story. It's funny. It doesn't like swimming.\nMch: So what's it doing? Eating?\nFch: Not now. It's playing a guitar! Look!",
            options = listOf("A (Playing electric guitar)", "B (Swimming in river)", "C (Eating sandwich)"),
            correctAnswer = "A (Playing electric guitar)",
            explanation = "The funny crocodile is playing a guitar."
        ),
        YleQuestionItem(
            id = "v2_starters_l_p3_3",
            partNumber = 3,
            partTitle = "Part 3: Listen and tick the box",
            instructions = "Listen to where Hugo's pens are located.",
            questionText = "3. Where are Hugo's pens?",
            audioTranscript = "Mch: I can't find my pens, Grandma. They aren't on my desk.\nF: Are those yours, Hugo? The pens under the lamp?\nMch: No, they're Sue's. Oh - mine are there, next to the TV. I can see them now.",
            options = listOf("A (Under the lamp)", "B (Next to the TV)", "C (On the study desk)"),
            correctAnswer = "B (Next to the TV)",
            explanation = "Hugo spots his pens next to the television."
        ),
        YleQuestionItem(
            id = "v2_starters_l_p3_4",
            partNumber = 3,
            partTitle = "Part 3: Listen and tick the box",
            instructions = "Listen to which boy is Sam.",
            questionText = "4. Which boy is Sam?",
            audioTranscript = "Fch: Look Dad. There's Sam. He's my new friend.\nM: The boy in the jeans and black jacket?\nFch: Not him. Sam's got a black jacket but he's wearing brown trousers.",
            options = listOf("A (Black jacket and brown trousers)", "B (Black jacket and blue jeans)", "C (Brown jacket and brown trousers)"),
            correctAnswer = "A (Black jacket and brown trousers)",
            explanation = "Sam is wearing a black jacket and brown trousers."
        ),
        YleQuestionItem(
            id = "v2_starters_l_p3_5",
            partNumber = 3,
            partTitle = "Part 3: Listen and tick the box",
            instructions = "Listen to which is the girl's favourite picture.",
            questionText = "5. Which is the girl's favourite picture?",
            audioTranscript = "Fch: These paintings are great, Mrs Door. Those flowers are beautiful.\nF: Thank you! Which painting is your favourite? The one of the houses?\nFch: That's nice, but no. My favourite is the one of the goats. I love that.",
            options = listOf("A (Street of colorful houses)", "B (Vase of yellow and pink flowers)", "C (Picture of goats in the mountains)"),
            correctAnswer = "C (Picture of goats in the mountains)",
            explanation = "The girl's favourite painting is the one with the goats."
        )
    )

    val startersReadingWritingV2 = listOf(
        // Part 1: Tick or Cross
        YleQuestionItem(
            id = "v2_starters_rw_p1_1",
            partNumber = 1,
            partTitle = "Part 1: Look and read. Put a tick (✔) or a cross (✘)",
            instructions = "Look at the hand mirror. Statement: 'This is a mat.'",
            questionText = "Picture of a hand mirror. Statement: 'This is a mat.'",
            options = listOf("✔ (True)", "✘ (False)"),
            correctAnswer = "✘ (False)",
            explanation = "It is a mirror, not a mat."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p1_2",
            partNumber = 1,
            partTitle = "Part 1: Look and read. Put a tick (✔) or a cross (✘)",
            instructions = "Look at the human ear. Statement: 'This is an ear.'",
            questionText = "Picture of an ear. Statement: 'This is an ear.'",
            options = listOf("✔ (True)", "✘ (False)"),
            correctAnswer = "✔ (True)",
            explanation = "The illustration shows an ear."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p1_3",
            partNumber = 1,
            partTitle = "Part 1: Look and read. Put a tick (✔) or a cross (✘)",
            instructions = "Look at the potatoes. Statement: 'These are coconuts.'",
            questionText = "Picture of two potatoes. Statement: 'These are coconuts.'",
            options = listOf("✔ (True)", "✘ (False)"),
            correctAnswer = "✘ (False)",
            explanation = "These are potatoes, not coconuts."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p1_4",
            partNumber = 1,
            partTitle = "Part 1: Look and read. Put a tick (✔) or a cross (✘)",
            instructions = "Look at the sofa. Statement: 'This is a sofa.'",
            questionText = "Picture of an orange sofa. Statement: 'This is a sofa.'",
            options = listOf("✔ (True)", "✘ (False)"),
            correctAnswer = "✔ (True)",
            explanation = "This is a sofa."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p1_5",
            partNumber = 1,
            partTitle = "Part 1: Look and read. Put a tick (✔) or a cross (✘)",
            instructions = "Look at the two helicopters. Statement: 'These are helicopters.'",
            questionText = "Picture of two helicopters in the sky. Statement: 'These are helicopters.'",
            options = listOf("✔ (True)", "✘ (False)"),
            correctAnswer = "✔ (True)",
            explanation = "These are two helicopters."
        ),

        // Part 2: Yes or No (Boys fishing at lake)
        YleQuestionItem(
            id = "v2_starters_rw_p2_1",
            partNumber = 2,
            partTitle = "Part 2: Look and read. Write yes or no",
            instructions = "Look at the picture with two boys fishing by a lake near a green tree and cows.",
            questionText = "1. The children are fishing in the sea.",
            options = listOf("yes", "no"),
            correctAnswer = "no",
            explanation = "They are fishing in a lake/pond, not the sea."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p2_2",
            partNumber = 2,
            partTitle = "Part 2: Look and read. Write yes or no",
            instructions = "Check the lake water for an old boot.",
            questionText = "2. There's an old boot in the water.",
            options = listOf("yes", "no"),
            correctAnswer = "no",
            explanation = "There is a frog and a fishing line in the water, not an old boot."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p2_3",
            partNumber = 2,
            partTitle = "Part 2: Look and read. Write yes or no",
            instructions = "Count the fish caught on the grass.",
            questionText = "3. You can see three fish in the picture.",
            options = listOf("yes", "no"),
            correctAnswer = "yes",
            explanation = "There are three fish lying on the grass next to the backpack."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p2_4",
            partNumber = 2,
            partTitle = "Part 2: Look and read. Write yes or no",
            instructions = "Check the child with black hair.",
            questionText = "4. The child with black hair is sitting down.",
            options = listOf("yes", "no"),
            correctAnswer = "yes",
            explanation = "The boy with black hair in the yellow shirt is sitting on a chair."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p2_5",
            partNumber = 2,
            partTitle = "Part 2: Look and read. Write yes or no",
            instructions = "Check the green frog by the bucket and backpack.",
            questionText = "5. There's a green frog on the bag.",
            options = listOf("yes", "no"),
            correctAnswer = "yes",
            explanation = "A green frog is sitting on top of the blue bucket/bag."
        ),

        // Part 3: Stationery words unscramble
        YleQuestionItem(
            id = "v2_starters_rw_p3_1",
            partNumber = 3,
            partTitle = "Part 3: Unscramble letters into school objects",
            instructions = "Unscramble: p - a - p - e - r",
            questionText = "Picture of a sheet of white paper: _ _ _ _ _",
            correctAnswer = "paper",
            acceptedAlternatives = listOf("PAPER", "Paper"),
            explanation = "p - a - p - e - r = paper."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p3_2",
            partNumber = 3,
            partTitle = "Part 3: Unscramble letters into school objects",
            instructions = "Unscramble: r - u - l - e - r",
            questionText = "Picture of a measuring ruler: _ _ _ _ _",
            correctAnswer = "ruler",
            acceptedAlternatives = listOf("RULER", "Ruler"),
            explanation = "r - u - l - e - r = ruler."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p3_3",
            partNumber = 3,
            partTitle = "Part 3: Unscramble letters into school objects",
            instructions = "Unscramble: e - r - a - s - e - r",
            questionText = "Picture of a blue rubber / eraser: _ _ _ _ _ _",
            correctAnswer = "eraser",
            acceptedAlternatives = listOf("ERASER", "Eraser"),
            explanation = "e - r - a - s - e - r = eraser."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p3_4",
            partNumber = 3,
            partTitle = "Part 3: Unscramble letters into school objects",
            instructions = "Unscramble: p - e - n - c - i - l",
            questionText = "Picture of a yellow pencil: _ _ _ _ _ _",
            correctAnswer = "pencil",
            acceptedAlternatives = listOf("PENCIL", "Pencil"),
            explanation = "p - e - n - c - i - l = pencil."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p3_5",
            partNumber = 3,
            partTitle = "Part 3: Unscramble letters into school objects",
            instructions = "Unscramble: t - e - a - c - h - e - r",
            questionText = "Picture of a school teacher at whiteboard: _ _ _ _ _ _ _",
            correctAnswer = "teacher",
            acceptedAlternatives = listOf("TEACHER", "Teacher"),
            explanation = "t - e - a - c - h - e - r = teacher."
        ),

        // Part 4: A Horse cloze
        YleQuestionItem(
            id = "v2_starters_rw_p4_1",
            partNumber = 4,
            partTitle = "Part 4: Read and fill the blanks (A horse)",
            instructions = "Word bank: [legs, people, food, balloon, eyes, door, day, tail]",
            questionText = "The horse has four long legs, two big (1) _______ and a long face.",
            options = listOf("eyes", "door", "food", "balloon", "day"),
            correctAnswer = "eyes",
            explanation = "The horse has two big eyes."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p4_2",
            partNumber = 4,
            partTitle = "Part 4: Read and fill the blanks (A horse)",
            instructions = "Word bank: [legs, people, food, balloon, eyes, door, day, tail]",
            questionText = "It has a brown (2) _______ on its body too.",
            options = listOf("tail", "door", "day", "food", "balloon"),
            correctAnswer = "tail",
            explanation = "A horse has a brown tail on its body."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p4_3",
            partNumber = 4,
            partTitle = "Part 4: Read and fill the blanks (A horse)",
            instructions = "Word bank: [legs, people, food, balloon, eyes, door, day, tail]",
            questionText = "Lots of (3) _______ enjoy riding horses. Some families have a horse for a pet.",
            options = listOf("people", "balloon", "door", "day", "eyes"),
            correctAnswer = "people",
            explanation = "Lots of people enjoy riding horses."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p4_4",
            partNumber = 4,
            partTitle = "Part 4: Read and fill the blanks (A horse)",
            instructions = "Word bank: [legs, people, food, balloon, eyes, door, day, tail]",
            questionText = "At the end of the (4) _______ , they clean their horse",
            options = listOf("day", "door", "food", "balloon", "legs"),
            correctAnswer = "day",
            explanation = "At the end of the day."
        ),
        YleQuestionItem(
            id = "v2_starters_rw_p4_5",
            partNumber = 4,
            partTitle = "Part 4: Read and fill the blanks (A horse)",
            instructions = "Word bank: [legs, people, food, balloon, eyes, door, day, tail]",
            questionText = "and give it (5) _______ and water. Horses like eating apples and carrots!",
            options = listOf("food", "door", "balloon", "tail", "people"),
            correctAnswer = "food",
            explanation = "They feed the horse food and water."
        )
    )

    // --- A1 MOVERS (VOL 2) ---
    val moversListeningV2 = listOf(
        YleQuestionItem(
            id = "v2_movers_l_p1_1",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people in the school music lesson",
            instructions = "Listen to Uncle and niece describing the music lesson.",
            questionText = "Who is Fred?",
            audioTranscript = "M: Who's the boy with the purple socks on? He's enjoying the lesson!\nFch: That's Fred. He's very clever. He can play the guitar and he can sing very well.",
            options = listOf("Boy in purple socks playing guitar", "Boy with curly hair playing piano", "Woman with bag laughing", "Girl with long hair writing words"),
            correctAnswer = "Boy in purple socks playing guitar",
            explanation = "Fred is wearing purple socks and playing the guitar."
        ),
        YleQuestionItem(
            id = "v2_movers_l_p1_2",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people in the music lesson",
            instructions = "Listen to who is playing the piano.",
            questionText = "Who is Mark?",
            audioTranscript = "Fch: And the boy with the curly hair is Mark.\nM: The one who's playing the piano?\nFch: Yes. He practises a lot! He loves music - like me.",
            options = listOf("Boy with curly hair playing piano", "Boy in blue jacket", "Girl with hat dancing", "Woman with bag"),
            correctAnswer = "Boy with curly hair playing piano",
            explanation = "Mark has curly hair and plays the piano."
        ),
        YleQuestionItem(
            id = "v2_movers_l_p2_1",
            partNumber = 2,
            partTitle = "Part 2: Grandma's work as a nurse",
            instructions = "Listen and write what Grandma had to wash in the morning.",
            questionText = "1. Had to wash: the _______ in the morning",
            audioTranscript = "Mch: What did you do in the mornings at the hospital?\nF: I washed the floor in the morning.\nMch: Do all the nurses have to wash the floors?\nF: Not now, but they did when I was young.",
            correctAnswer = "floor",
            acceptedAlternatives = listOf("floors", "the floor", "the floors", "Floor", "FLOOR"),
            explanation = "Grandma washed the floors in the mornings."
        ),
        YleQuestionItem(
            id = "v2_movers_l_p2_2",
            partNumber = 2,
            partTitle = "Part 2: Grandma's work as a nurse",
            instructions = "Listen and write the number of work days per week.",
            questionText = "2. Number of work days every week:",
            audioTranscript = "Mch: How many days of the week did you work?\nF: Oh. I only worked four days.\nMch: Four days isn't very long, Grandma!",
            correctAnswer = "4",
            acceptedAlternatives = listOf("four", "Four", "FOUR", "4 days"),
            explanation = "Grandma worked 4 days every week."
        ),
        YleQuestionItem(
            id = "v2_movers_l_p2_3",
            partNumber = 2,
            partTitle = "Part 2: Grandma's work as a nurse",
            instructions = "Listen and write what color clothes she wore.",
            questionText = "3. Wore: blue _______",
            audioTranscript = "Mch: What clothes did you have to wear for work?\nF: I wore a blue dress every day.\nMch: A blue dress? Every day?\nF: Yes, but I always took it off when I got home.",
            correctAnswer = "dress",
            acceptedAlternatives = listOf("Dress", "DRESS", "a dress"),
            explanation = "She wore a blue dress every day."
        ),
        YleQuestionItem(
            id = "v2_movers_l_p2_4",
            partNumber = 2,
            partTitle = "Part 2: Grandma's work as a nurse",
            instructions = "Listen and write what she had lots of at work.",
            questionText = "4. At work, Grandma had: lots of _______",
            audioTranscript = "F: Yes. I enjoyed working at the hospital a lot because I had lots of friends there.\nMch: Did you?\nF: Yes. It's good to have lots of friends at work.",
            correctAnswer = "friends",
            acceptedAlternatives = listOf("Friends", "FRIENDS", "friend"),
            explanation = "Grandma had lots of friends at work."
        ),
        YleQuestionItem(
            id = "v2_movers_l_p2_5",
            partNumber = 2,
            partTitle = "Part 2: Grandma's work as a nurse",
            instructions = "Listen and write who the hospital was for.",
            questionText = "5. The hospital was for: _______ only",
            audioTranscript = "Mch: Who came to your hospital?\nF: Our hospital was only for women.\nMch: Only for women who were ill?\nF: Yes, but they always got better because the doctors were very clever.",
            correctAnswer = "women",
            acceptedAlternatives = listOf("ill women", "women who were ill", "Women", "WOMEN"),
            explanation = "The hospital treated ill women only."
        ),
        YleQuestionItem(
            id = "v2_movers_l_p4_1",
            partNumber = 4,
            partTitle = "Part 4: Listen and tick the box",
            instructions = "Listen to which man is the girl's teacher.",
            questionText = "1. Which man is the girl's teacher?",
            audioTranscript = "Fch: There's my teacher! Can you see him?\nMch: The man with the fair hair?\nFch: Yes. He's wearing his black shirt today ... Mom doesn't like his new beard but I do!",
            options = listOf("A (Fair hair, black shirt, beard)", "B (Fair hair, black shirt, no beard)", "C (Fair hair, blue blazer jacket, no beard)"),
            correctAnswer = "A (Fair hair, black shirt, beard)",
            explanation = "The teacher has fair hair, wears a black shirt and has a beard (Picture A)."
        ),
        YleQuestionItem(
            id = "v2_movers_l_p4_2",
            partNumber = 4,
            partTitle = "Part 4: Listen and tick the box",
            instructions = "Listen to what Sally lost.",
            questionText = "2. What did Sally lose?",
            audioTranscript = "M: What's the matter, Sally?\nFch: My baseball cap's in my sports bag, but you know my blue towel? That's not here.\nM: Well, go and get another one from the bathroom.",
            options = listOf("A (Baseball cap)", "B (Blue towel)", "C (Blue swimsuit)"),
            correctAnswer = "B (Blue towel)",
            explanation = "Sally lost her blue towel (Picture B)."
        )
    )

    val moversReadingWritingV2 = listOf(
        // Part 1: Definitions
        YleQuestionItem(
            id = "v2_movers_rw_p1_1",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a balcony, a kangaroo, a swimsuit, a helmet, a panda, lifts, stairs, a dolphin]",
            questionText = "1. You can put chairs and tables on this and sit outside.",
            options = listOf("a balcony", "lifts", "stairs", "a swimsuit"),
            correctAnswer = "a balcony",
            explanation = "A balcony is an outdoor terrace on a building."
        ),
        YleQuestionItem(
            id = "v2_movers_rw_p1_2",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a balcony, a kangaroo, a swimsuit, a helmet, a panda, lifts, stairs, a dolphin]",
            questionText = "2. This is black and white and lives in the mountains.",
            options = listOf("a panda", "a kangaroo", "a dolphin", "a helmet"),
            correctAnswer = "a panda",
            explanation = "A panda is a black and white bear native to mountain forests."
        ),
        YleQuestionItem(
            id = "v2_movers_rw_p1_3",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a balcony, a kangaroo, a swimsuit, a helmet, a panda, lifts, stairs, a dolphin]",
            questionText = "3. You put this on your head and then ride your bike.",
            options = listOf("a helmet", "a swimsuit", "a balcony", "lifts"),
            correctAnswer = "a helmet",
            explanation = "A helmet protects your head while cycling."
        ),
        YleQuestionItem(
            id = "v2_movers_rw_p1_4",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a balcony, a kangaroo, a swimsuit, a helmet, a panda, lifts, stairs, a dolphin]",
            questionText = "4. You can walk up and down these inside a house.",
            options = listOf("stairs", "lifts", "a balcony", "a kangaroo"),
            correctAnswer = "stairs",
            explanation = "Stairs connect different levels within a building."
        ),
        YleQuestionItem(
            id = "v2_movers_rw_p1_5",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a balcony, a kangaroo, a swimsuit, a helmet, a panda, lifts, stairs, a dolphin]",
            questionText = "5. This animal swims and sometimes jumps above the waves.",
            options = listOf("a dolphin", "a panda", "a kangaroo", "a swimsuit"),
            correctAnswer = "a dolphin",
            explanation = "A dolphin is an agile marine mammal that leaps above waves."
        ),

        // Part 4: Parrots cloze
        YleQuestionItem(
            id = "v2_movers_rw_p4_1",
            partNumber = 4,
            partTitle = "Part 4: Choose the right word (Parrots)",
            instructions = "Choose the relative pronoun for animals.",
            questionText = "A lot of parrots are green, but you can find parrots _______ are red, yellow and blue.",
            options = listOf("which", "what", "where"),
            correctAnswer = "which",
            explanation = "'Which' refers to things/animals (parrots)."
        ),
        YleQuestionItem(
            id = "v2_movers_rw_p4_2",
            partNumber = 4,
            partTitle = "Part 4: Choose the right word (Parrots)",
            instructions = "Choose the preposition following 'good'.",
            questionText = "They have big heads and short necks. They are very good _______ climbing trees.",
            options = listOf("at", "on", "of"),
            correctAnswer = "at",
            explanation = "'Good at' + gerund verb."
        ),
        YleQuestionItem(
            id = "v2_movers_rw_p4_3",
            partNumber = 4,
            partTitle = "Part 4: Choose the right word (Parrots)",
            instructions = "Choose the quantifier.",
            questionText = "_______ parrots do not eat meat. They eat fruit and plants.",
            options = listOf("Most", "Every", "Both"),
            correctAnswer = "Most",
            explanation = "'Most' takes a plural noun without article."
        ),
        YleQuestionItem(
            id = "v2_movers_rw_p4_4",
            partNumber = 4,
            partTitle = "Part 4: Choose the right word (Parrots)",
            instructions = "Choose the continuous verb.",
            questionText = "When they are _______ , they hold their food in one foot.",
            options = listOf("eating", "ate", "eat"),
            correctAnswer = "eating",
            explanation = "Present continuous 'are eating'."
        ),
        YleQuestionItem(
            id = "v2_movers_rw_p4_5",
            partNumber = 4,
            partTitle = "Part 4: Choose the right word (Parrots)",
            instructions = "Choose the demonstrative pronoun.",
            questionText = "_______ birds make a lot of noise when they are with their families.",
            options = listOf("These", "That", "This"),
            correctAnswer = "These",
            explanation = "'These' is plural and refers to the parrots mentioned."
        )
    )

    // --- A2 FLYERS (VOL 2) ---
    val flyersListeningV2 = listOf(
        YleQuestionItem(
            id = "v2_flyers_l_p1_1",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people at the beach",
            instructions = "Listen to the beach dialogue.",
            questionText = "Who is Emma?",
            audioTranscript = "F: Do you know the girls who are playing volleyball?\nMch: I know one of them - the girl in the pink skirt.\nF: What's her name?\nMch: Emma. Her brother's a friend of mine.",
            options = listOf("Girl in pink skirt playing volleyball", "Man looking angry pointing to ball", "Boy in blue shorts holding shell", "Girl wearing hat with net"),
            correctAnswer = "Girl in pink skirt playing volleyball",
            explanation = "Emma is the girl in the pink skirt playing volleyball."
        ),
        YleQuestionItem(
            id = "v2_flyers_l_p1_2",
            partNumber = 1,
            partTitle = "Part 1: Listen and match people at the beach",
            instructions = "Listen to who was sleeping under the umbrella.",
            questionText = "Who is William?",
            audioTranscript = "F: That man doesn't look very happy!\nMch: Oh, you mean William? Well, he was asleep, but he woke up when the volleyball hit him on the head!",
            options = listOf("Man looking angry pointing to ball", "Boy pushing other into sea", "Boy with shell", "Girl playing volleyball"),
            correctAnswer = "Man looking angry pointing to ball",
            explanation = "William is the man who woke up when the volleyball hit him."
        ),
        YleQuestionItem(
            id = "v2_flyers_l_p2_1",
            partNumber = 2,
            partTitle = "Part 2: New Homes phone message about a house",
            instructions = "Listen and spell the street name.",
            questionText = "1. Address: 12 _______ Street",
            audioTranscript = "M: Where is the house? I mean, what's the address?\nFch: It's number 12 Princes Street.\nM: How do you spell the name of the street? I want to write it down.\nFch: It's P-R-I-N-C-E-S.",
            correctAnswer = "Princes",
            acceptedAlternatives = listOf("PRINCES", "P-R-I-N-C-E-S", "princes", "Prince's"),
            explanation = "12 Princes Street (P-R-I-N-C-E-S)."
        ),
        YleQuestionItem(
            id = "v2_flyers_l_p2_2",
            partNumber = 2,
            partTitle = "Part 2: New Homes phone message",
            instructions = "Listen to where the house is located.",
            questionText = "2. It's near the: _______",
            audioTranscript = "Fch: No, it isn't. It's near the library. It's a nice quiet street, he said.",
            correctAnswer = "library",
            acceptedAlternatives = listOf("the library", "Library", "LIBRARY"),
            explanation = "The house is near the library."
        ),
        YleQuestionItem(
            id = "v2_flyers_l_p2_3",
            partNumber = 2,
            partTitle = "Part 2: New Homes phone message",
            instructions = "Listen to where the smaller bathroom is.",
            questionText = "3. Smaller bathroom is: _______",
            audioTranscript = "Fch: There are two bathrooms: a big one upstairs and a smaller one downstairs.",
            correctAnswer = "downstairs",
            acceptedAlternatives = listOf("Downstairs", "DOWNSTAIRS"),
            explanation = "The smaller bathroom is downstairs."
        ),
        YleQuestionItem(
            id = "v2_flyers_l_p2_4",
            partNumber = 2,
            partTitle = "Part 2: New Homes phone message",
            instructions = "Listen to what is in the garden.",
            questionText = "4. Garden has: a _______",
            audioTranscript = "M: And is there a swimming pool in the garden?\nFch: No, Dad. There's a swing in it.",
            correctAnswer = "swing",
            acceptedAlternatives = listOf("Swing", "SWING", "a swing"),
            explanation = "The garden has a swing."
        ),
        YleQuestionItem(
            id = "v2_flyers_l_p2_5",
            partNumber = 2,
            partTitle = "Part 2: New Homes phone message",
            instructions = "Listen to where the music room is located.",
            questionText = "5. There's a music room in: the _______",
            audioTranscript = "M: Has it? Well, that's something different. Where is it?\nFch: It's in the basement. I could play my pop music and drums really loudly!",
            correctAnswer = "basement",
            acceptedAlternatives = listOf("the basement", "Basement", "BASEMENT"),
            explanation = "The music room is in the basement."
        ),
        YleQuestionItem(
            id = "v2_flyers_l_p4_1",
            partNumber = 4,
            partTitle = "Part 4: Listen and tick the box",
            instructions = "Listen to which shirt Frank wants to pack.",
            questionText = "1. Which shirt does Frank want to take on holiday?",
            audioTranscript = "Mch: Well, I don't want that striped one - it's too small now.\nF: Mm. You're right. What about this one with the butterflies on it?\nMch: No, thanks. I hate that shirt. I want to take that black one.",
            options = listOf("A (Blue shirt with butterflies)", "B (Red striped shirt)", "C (Plain black shirt)"),
            correctAnswer = "C (Plain black shirt)",
            explanation = "Frank chooses the plain black shirt (Picture C)."
        ),
        YleQuestionItem(
            id = "v2_flyers_l_p4_2",
            partNumber = 4,
            partTitle = "Part 4: Listen and tick the box",
            instructions = "Listen to where the plane tickets are found.",
            questionText = "2. Where are the tickets?",
            audioTranscript = "Mch: Have you looked in your handbag?\nF: Yes, of course I have. Oh, here they are - on the desk, next to this book!",
            options = listOf("A (On the desk next to a book)", "B (On a wooden bench)", "C (Inside red handbag)"),
            correctAnswer = "A (On the desk next to a book)",
            explanation = "The tickets are on the desk next to the book (Picture A)."
        )
    )

    val flyersReadingWritingV2 = listOf(
        // Part 1: Advanced Vocabulary Definitions
        YleQuestionItem(
            id = "v2_flyers_rw_p1_1",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a diary, a businessman, a calendar, a desert, an ocean, caves, a tyre, an engine, a waiter, a dentist, a postcard, a dictionary, a motorway, woods, an ambulance]",
            questionText = "1. These are dark, cold places inside mountains, and sometimes bats live in them.",
            options = listOf("caves", "woods", "a desert", "an ocean"),
            correctAnswer = "caves",
            explanation = "Caves are subterranean hollows where bats roost."
        ),
        YleQuestionItem(
            id = "v2_flyers_rw_p1_2",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a diary, a businessman, a calendar, a desert, an ocean, caves, a tyre, an engine, a waiter, a dentist, a postcard, a dictionary, a motorway, woods, an ambulance]",
            questionText = "2. A driver takes people who are very ill to see doctors in hospital in this.",
            options = listOf("an ambulance", "a motorway", "an engine", "a tyre"),
            correctAnswer = "an ambulance",
            explanation = "An ambulance is an emergency medical transport vehicle."
        ),
        YleQuestionItem(
            id = "v2_flyers_rw_p1_3",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a diary, a businessman, a calendar, a desert, an ocean, caves, a tyre, an engine, a waiter, a dentist, a postcard, a dictionary, a motorway, woods, an ambulance]",
            questionText = "3. If you can't spell a word, you can look for the meanings and spellings of different words in this.",
            options = listOf("a dictionary", "a diary", "a calendar", "a postcard"),
            correctAnswer = "a dictionary",
            explanation = "A dictionary contains words, definitions, and spelling rules."
        ),
        YleQuestionItem(
            id = "v2_flyers_rw_p1_4",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a diary, a businessman, a calendar, a desert, an ocean, caves, a tyre, an engine, a waiter, a dentist, a postcard, a dictionary, a motorway, woods, an ambulance]",
            questionText = "4. This person carries meals on plates to people in restaurants.",
            options = listOf("a waiter", "a dentist", "a businessman", "an ambulance"),
            correctAnswer = "a waiter",
            explanation = "A waiter serves food and beverages at dining establishments."
        ),
        YleQuestionItem(
            id = "v2_flyers_rw_p1_5",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a diary, a businessman, a calendar, a desert, an ocean, caves, a tyre, an engine, a waiter, a dentist, a postcard, a dictionary, a motorway, woods, an ambulance]",
            questionText = "5. This is a very dry place where there is usually sand on the ground.",
            options = listOf("a desert", "woods", "an ocean", "caves"),
            correctAnswer = "a desert",
            explanation = "A desert is an arid biome with minimal rainfall and dunes."
        ),
        YleQuestionItem(
            id = "v2_flyers_rw_p1_6",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a diary, a businessman, a calendar, a desert, an ocean, caves, a tyre, an engine, a waiter, a dentist, a postcard, a dictionary, a motorway, woods, an ambulance]",
            questionText = "6. If you go for a walk in these, you see some trees and perhaps birds, too.",
            options = listOf("woods", "caves", "a desert", "a motorway"),
            correctAnswer = "woods",
            explanation = "Woods are forested areas with trees and wildlife."
        ),
        YleQuestionItem(
            id = "v2_flyers_rw_p1_7",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a diary, a businessman, a calendar, a desert, an ocean, caves, a tyre, an engine, a waiter, a dentist, a postcard, a dictionary, a motorway, woods, an ambulance]",
            questionText = "7. You go and see this person if you have a hole in your tooth.",
            options = listOf("a dentist", "a waiter", "a businessman", "a doctor"),
            correctAnswer = "a dentist",
            explanation = "A dentist treats teeth, cavities and oral health."
        ),
        YleQuestionItem(
            id = "v2_flyers_rw_p1_8",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a diary, a businessman, a calendar, a desert, an ocean, caves, a tyre, an engine, a waiter, a dentist, a postcard, a dictionary, a motorway, woods, an ambulance]",
            questionText = "8. This is something you write on and send to a friend when you are on holiday.",
            options = listOf("a postcard", "a diary", "a calendar", "a dictionary"),
            correctAnswer = "a postcard",
            explanation = "A postcard is mailed with greetings from travel destinations."
        ),
        YleQuestionItem(
            id = "v2_flyers_rw_p1_9",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a diary, a businessman, a calendar, a desert, an ocean, caves, a tyre, an engine, a waiter, a dentist, a postcard, a dictionary, a motorway, woods, an ambulance]",
            questionText = "9. This is a kind of road where you can travel very quickly from city to city.",
            options = listOf("a motorway", "woods", "a desert", "an engine"),
            correctAnswer = "a motorway",
            explanation = "A motorway (highway) provides high-speed transit between cities."
        ),
        YleQuestionItem(
            id = "v2_flyers_rw_p1_10",
            partNumber = 1,
            partTitle = "Part 1: Match definitions to words",
            instructions = "Word bank: [a diary, a businessman, a calendar, a desert, an ocean, caves, a tyre, an engine, a waiter, a dentist, a postcard, a dictionary, a motorway, woods, an ambulance]",
            questionText = "10. You find this round and black thing on the wheels of cars and motorbikes.",
            options = listOf("a tyre", "an engine", "a motorway", "a calendar"),
            correctAnswer = "a tyre",
            explanation = "A tyre (tire) is the rubber ring fitted to vehicle wheels."
        )
    )

    // --- SPEAKING SIMULATIONS (VOL 1 & VOL 2) ---
    val speakingSimulationsV1 = listOf(
        YleQuestionItem(
            id = "v1_speaking_starters",
            partNumber = 1,
            partTitle = "Pre A1 Starters Vol 1: Speaking Examiner Simulation",
            instructions = "Examiner: Look at the kitchen scene and the object cards (robot, crocodile, carrot, T-shirt, juice, frog, pen, cap).",
            questionText = "Examiner prompt: 'Where is the red chair? Now put the robot on the red chair.'",
            audioTranscript = "Examiner: Hello. What's your name? ... Look at this picture. This is a kitchen. The boy is reading a comic. Where's the clock? ... And where are the bananas? ... Now look at these cards. Which is the robot? Put the robot on the red chair.",
            options = listOf("Put robot on the red chair", "Put robot under table", "Put crocodile on cupboard", "Put carrot in fridge"),
            correctAnswer = "Put robot on the red chair",
            explanation = "Candidate successfully places the robot card onto the red chair."
        ),
        YleQuestionItem(
            id = "v1_speaking_movers",
            partNumber = 2,
            partTitle = "A1 Movers Vol 1: Find the Differences & Story 'Fred loves food'",
            instructions = "Find the differences between the underwater/diver scenes and continue the story.",
            questionText = "Examiner: 'In my picture, there is a toy boat near the diver. What is there in your picture?'",
            audioTranscript = "Examiner: In my picture, the sun is shining in the sky. In your picture, there are clouds. In my picture, the diver has a toy boat in her hand. In your picture, she has a striped beach ball.",
            options = listOf("A striped beach ball", "A toy boat", "A big dolphin", "Two yellow fish"),
            correctAnswer = "A striped beach ball",
            explanation = "In candidate's picture, the diver holds a striped beach ball instead of a toy boat."
        ),
        YleQuestionItem(
            id = "v1_speaking_flyers",
            partNumber = 3,
            partTitle = "A2 Flyers Vol 1: Information Exchange & Story 'The Brave Teacher'",
            instructions = "Exchange information about Robert's vs Sarah's favourite restaurants.",
            questionText = "Ask the examiner about Sarah's favourite restaurant: 'What is the name of Sarah's favourite restaurant?'",
            audioTranscript = "Candidate: What's the name of Sarah's favourite restaurant?\nExaminer: It's called 'Rainbows'.\nCandidate: What does she like eating there?\nExaminer: She loves eating pizza.\nCandidate: Where is it?\nExaminer: It's on Hill Street and opens at 12:30. It's cheap!",
            options = listOf("Rainbows (on Hill Street, serves pizza, cheap)", "The Black Cat (on North Street, expensive)", "Jungle Café", "Castle Diner"),
            correctAnswer = "Rainbows (on Hill Street, serves pizza, cheap)",
            explanation = "Sarah's favourite restaurant is 'Rainbows' on Hill Street."
        )
    )

    val speakingSimulationsV2 = listOf(
        YleQuestionItem(
            id = "v2_speaking_starters",
            partNumber = 1,
            partTitle = "Pre A1 Starters Vol 2: Speaking Examiner Simulation",
            instructions = "Examiner: Look at the boat and beach scene and object cards (apple, pencil, kite, spider, chocolate, milk, ruler, wardrobe).",
            questionText = "Examiner prompt: 'Where are the pink fish? Now put the apple in front of the birds.'",
            audioTranscript = "Examiner: Hello! What's your name? ... Look at this picture. The children are on a boat at the beach. Where is the sun? ... And where is the yellow hat? ... Now look at these cards. Which is the apple? Put the apple in front of the birds on the rock.",
            options = listOf("Put the green apple in front of the birds", "Put the spider in the boat", "Put the milk on the umbrella", "Put pencil in sea"),
            correctAnswer = "Put the green apple in front of the birds",
            explanation = "Candidate correctly identifies the green apple and places it in front of the birds on the rock."
        ),
        YleQuestionItem(
            id = "v2_speaking_movers",
            partNumber = 2,
            partTitle = "A1 Movers Vol 2: Find the Differences & Story 'The Windy Day'",
            instructions = "Find differences in the bedroom (girl with headache / computer / fish) and describe 'The Windy Day'.",
            questionText = "Examiner: 'In my picture, the laptop is on the table next to the bed. What about your picture?'",
            audioTranscript = "Examiner: In my picture, the laptop is on the floor next to the bed. In your picture, the laptop is on the green blanket. In my picture, there is one goldfish in the bowl, but in your picture there are two!",
            options = listOf("The laptop is on the bed (green blanket)", "The laptop is under the bed", "There are no laptops", "The lamp is green"),
            correctAnswer = "The laptop is on the bed (green blanket)",
            explanation = "In candidate's picture, the laptop rests on the green blanket on the bed."
        ),
        YleQuestionItem(
            id = "v2_speaking_flyers",
            partNumber = 3,
            partTitle = "A2 Flyers Vol 2: Information Exchange 'George's Castle vs Grace's Castle' & Story 'Grandma's busy day'",
            instructions = "Exchange information about Grace's castle and George's castle.",
            questionText = "Ask the examiner about Grace's castle: 'Where is Grace's castle and who lives there?'",
            audioTranscript = "Candidate: Where is Grace's castle?\nExaminer: It's in the forest.\nCandidate: What is the name of Grace's castle?\nExaminer: It's called Silver Castle.\nCandidate: Who lives there and how old is it?\nExaminer: An artist lives there and it is 1000 years old!",
            options = listOf("Silver Castle (in the forest, an artist lives there, 1000 years old)", "Black Castle (on the mountain, queen lives there, 500 years old)", "Castle Rock (near the lake)", "Green Hill Castle"),
            correctAnswer = "Silver Castle (in the forest, an artist lives there, 1000 years old)",
            explanation = "Grace's castle is Silver Castle in the forest, inhabited by an artist and 1000 years old."
        )
    )
}
