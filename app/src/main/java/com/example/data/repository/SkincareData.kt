package com.example.data.repository

import com.example.data.model.BudgetRange
import com.example.data.model.IngredientItem
import com.example.data.model.Recipe

object SkincareData {

    val receitasEconomicas = listOf(
        Recipe(
            id = "rec_babosa_mel",
            titulo = "Máscara Calmante de Babosa e Mel Puro",
            subTitulo = "Anti-inflamatória, reduz espinhas e hidrata profundamente",
            categoriaPreco = BudgetRange.ATE_500,
            custoEstimadoKz = 450,
            tempoPreparo = "5 minutos",
            frequencia = "2 a 3 vezes por semana à noite",
            tipoPeleRecomendada = "Oleosa, Mista ou com Acne",
            beneficioPrincipal = "Seca espinhas sem ressecar e acalma vermelhidão",
            ingredientes = listOf(
                IngredientItem("Gel de Babosa fresca (Aloe Vera)", "2 colheres de sopa", 200),
                IngredientItem("Mel puro de abelha", "1 colher de chá", 250)
            ),
            passoAPasso = listOf(
                "Corte uma folha de babosa, deixe o líquido amarelo escorrer por 10 minutos (pois pode irritar a pele) e lave bem.",
                "Retire o gel transparente de dentro da folha com uma colher.",
                "Em um potinho limpo, misture o gel de babosa com 1 colher de chá de mel puro.",
                "Lave o rosto com água morna e aplique a mistura suavemente com as pontas dos dedos limpas.",
                "Deixe agir de 15 a 20 minutos.",
                "Enxágue abundantemente com água fresca e aplique o seu hidratante suave."
            ),
            avisosSeguranca = "NUNCA use o líquido amarelado da casca da babosa (aloína), pois pode causar coceira e alergia. Use apenas o gel transparente interno.",
            requerTesteAlergia = true
        ),
        Recipe(
            id = "rec_agua_arroz",
            titulo = "Tónico Iluminador de Água de Arroz Fermentada",
            subTitulo = "Tradição asiática para clarear manchas e fechar poros",
            categoriaPreco = BudgetRange.ATE_500,
            custoEstimadoKz = 300,
            tempoPreparo = "10 minutos (+ repouso)",
            frequencia = "Diariamente após lavar o rosto",
            tipoPeleRecomendada = "Todos os tipos de pele, especialmente com manchas",
            beneficioPrincipal = "Rico em inositol e ácido ferúlico, suaviza manchas e acalma",
            ingredientes = listOf(
                IngredientItem("Arroz cru (qualquer tipo)", "Meia xícara", 200),
                IngredientItem("Água mineral ou fervida", "1 xícara e meia", 100)
            ),
            passoAPasso = listOf(
                "Lave o arroz uma vez rapidamente para tirar impurezas e descarte essa primeira água.",
                "Coloque o arroz numa tigela com água limpa e deixe descansar por 30 minutos a 2 horas (ou fermentar levemente por 12 horas).",
                "Mexa bem com uma colher até a água ficar leitosa e escorra para um frasco limpo.",
                "Guarde no frigorífico por até 5 dias.",
                "Aplique no rosto com um pedaço de algodão limpo após a higienização, sem enxaguar."
            ),
            avisosSeguranca = "Descarte a mistura após 5 dias na geladeira para evitar proliferação bacteriana.",
            requerTesteAlergia = false
        ),
        Recipe(
            id = "rec_aveia_iogurte",
            titulo = "Máscara Esfoliante Suave de Aveia e Iogurte",
            subTitulo = "Remove células mortas sem agredir peles sensíveis",
            categoriaPreco = BudgetRange.ATE_1500,
            custoEstimadoKz = 1100,
            tempoPreparo = "5 minutos",
            frequencia = "1 vez por semana",
            tipoPeleRecomendada = "Seca, Normal ou Sensível",
            beneficioPrincipal = "Esfoliação química suave pelo ácido lático e nutrição da aveia",
            ingredientes = listOf(
                IngredientItem("Flocos finos de aveia", "2 colheres de sopa", 400),
                IngredientItem("Iogurte natural sem açúcar", "2 colheres de sopa", 700)
            ),
            passoAPasso = listOf(
                "Misture a aveia e o iogurte natural até formar uma pasta cremosa homogênea.",
                "Deixe descansar por 3 minutos para a aveia amolecer e liberar as beta-glucanas.",
                "Aplique no rosto limpo fazendo movimentos circulares ultra suaves, sem pressionar.",
                "Deixe agir por 10 minutos para o ácido lático agir.",
                "Enxágue com água morna e finalize com protetor solar ou hidratante."
            ),
            avisosSeguranca = "Não esfregue com força. Se tiver alergia a laticínios ou intolerância na pele, evite.",
            requerTesteAlergia = true
        ),
        Recipe(
            id = "rec_cha_verde_olheiras",
            titulo = "Compressa Descongestionante de Chá Verde e Pepino",
            subTitulo = "Reduz o inchaço matinal, olheiras arroxeadas e cansaço",
            categoriaPreco = BudgetRange.ATE_1500,
            custoEstimadoKz = 850,
            tempoPreparo = "10 minutos",
            frequencia = "Manhãs ou antes de eventos",
            tipoPeleRecomendada = "Todos os tipos de pele",
            beneficioPrincipal = "Cafeína e antioxidantes contraem vasos sanguíneos diminuindo bolsas",
            ingredientes = listOf(
                IngredientItem("Saqueta de chá verde puro", "1 unidade", 350),
                IngredientItem("Pepino fresco fatiado gelado", "4 rodelas", 500)
            ),
            passoAPasso = listOf(
                "Faça uma infusão concentrada com a saqueta de chá verde em meia chávena de água quente.",
                "Retire a saqueta e coloque na geladeira por 15 minutos até ficar bem gelada.",
                "Coloque as rodelas de pepino embebidas no chá gelado sobre os olhos fechados e olheiras.",
                "Relaxe deitada por 10 a 15 minutos.",
                "Lave suavemente o rosto com água fresca."
            ),
            avisosSeguranca = "Certifique-se de que o chá verde não possui aromas artificiais ou pimenta adicionada.",
            requerTesteAlergia = false
        ),
        Recipe(
            id = "rec_curcuma_anti_manchas",
            titulo = "Máscara Dourada Iluminadora de Cúrcuma e Mel",
            subTitulo = "Uniformiza o tom de pele e atenua manchas pós-acne",
            categoriaPreco = BudgetRange.ATE_3000,
            custoEstimadoKz = 1800,
            tempoPreparo = "5 minutos",
            frequencia = "1 vez por semana exclusivamente à noite",
            tipoPeleRecomendada = "Mista, Oleosa e com Manchas Escuras",
            beneficioPrincipal = "A curcumina combate manchas escuras e radicais livres",
            ingredientes = listOf(
                IngredientItem("Açafrão-da-terra (Cúrcuma pura em pó)", "Meia colher de café", 600),
                IngredientItem("Mel puro de abelha", "1 colher de sopa", 500),
                IngredientItem("Gel de babosa ou iogurte natural", "1 colher de chá", 700)
            ),
            passoAPasso = listOf(
                "Misture uma quantidade bem pequena de cúrcuma com o mel e a babosa até dissolver bem.",
                "Aplique apenas nas manchas ou no rosto com pincel limpo (a cúrcuma pode amarelar os dedos).",
                "Deixe agir por no máximo 10 a 12 minutos.",
                "Remova com água morna e sabonete suave. Use uma toalha escura para secar.",
                "No dia seguinte, use SEMPRE protetor solar."
            ),
            avisosSeguranca = "Cuidado com a dosagem: use pouca cúrcuma para não tingir a pele temporariamente. NUNCA se exponha ao sol com a máscara.",
            requerTesteAlergia = true
        )
    )

    data class SimpleProductRecommendation(
        val id: String,
        val nome: String,
        val tipo: String,
        val faixaPrecoKz: String,
        val precoEstimadoKz: Int,
        val indicadoPara: String,
        val comoUsar: String,
        val ondeEncontrar: String
    )

    val produtosSimplesAcessiveis = listOf(
        SimpleProductRecommendation(
            id = "prod_sabonete_neutro",
            nome = "Sabonete Neutro de Glicerina / Bebé",
            tipo = "Limpeza Facial Diária",
            faixaPrecoKz = "400 - 700 Kz",
            precoEstimadoKz = 500,
            indicadoPara = "Todos os tipos de pele, sensível e seca",
            comoUsar = "Lavar de manhã e à noite com movimentos circulares, sem esfregar.",
            ondeEncontrar = "Farmácias locais, armazéns e supermercados populares"
        ),
        SimpleProductRecommendation(
            id = "prod_sabonete_enxofre",
            nome = "Sabonete de Enxofre 5% a 10%",
            tipo = "Controle de Acne e Oleosidade",
            faixaPrecoKz = "600 - 1.000 Kz",
            precoEstimadoKz = 800,
            indicadoPara = "Pele muito oleosa com cravos e espinhas ativas",
            comoUsar = "Usar apenas 3x por semana à noite para não causar efeito rebote.",
            ondeEncontrar = "Farmácias de bairro e drogarias"
        ),
        SimpleProductRecommendation(
            id = "prod_creme_sorbolene",
            nome = "Creme Hidratante Aquoso Neutro / Glicerina",
            tipo = "Hidratação de Barreira",
            faixaPrecoKz = "1.200 - 1.800 Kz",
            precoEstimadoKz = 1500,
            indicadoPara = "Pele ressecada, sensível ou pós-sol",
            comoUsar = "Aplicar uma camada fina logo após lavar o rosto ainda ligeiramente húmido.",
            ondeEncontrar = "Supermercados e farmácias"
        ),
        SimpleProductRecommendation(
            id = "prod_locao_calamina",
            nome = "Loção de Calamina com Óxido de Zinco",
            tipo = "Secativo e Calmante",
            faixaPrecoKz = "900 - 1.500 Kz",
            precoEstimadoKz = 1200,
            indicadoPara = "Secar espinhas pontuais e aliviar queimaduras solares",
            comoUsar = "Com um cotonete limpo, encoste apenas em cima da espinha antes de dormir.",
            ondeEncontrar = "Qualquer farmácia em Angola"
        ),
        SimpleProductRecommendation(
            id = "prod_protetor_solar_acessivel",
            nome = "Protetor Solar Facial Básico FPS 30 a 50",
            tipo = "Proteção Solar Diária",
            faixaPrecoKz = "2.500 - 3.500 Kz",
            precoEstimadoKz = 2900,
            indicadoPara = "Essencial para TODAS! Previne manchas e envelhecimento",
            comoUsar = "Aplicar 2 dedos de protetor todas as manhãs, mesmo em dias nublados.",
            ondeEncontrar = "Farmácias e lojas de cosméticos acessíveis"
        )
    )

    data class SafetyAlert(
        val tituloPerigo: String,
        val ingredientesProibidos: String,
        val porqueEPerigoso: String,
        val oQueFazerEmVezDisso: String
    )

    val alertasDeSeguranca = listOf(
        SafetyAlert(
            tituloPerigo = "Limão puro no rosto e exposição ao Sol",
            ingredientesProibidos = "Sumo de limão, lima ou frutos cítricos",
            porqueEPerigoso = "O ácido cítrico e o psoraleno reagem com os raios ultravioleta do sol causando queimaduras químicas graves de segundo grau e manchas pretas persistentes (fitofotodermatose).",
            oQueFazerEmVezDisso = "Nunca coloque limão no rosto. Para clarear manchas com segurança use água de arroz ou gel de babosa."
        ),
        SafetyAlert(
            tituloPerigo = "Bicarbonato de Sódio puro no rosto",
            ingredientesProibidos = "Bicarbonato de sódio como esfoliante ou máscara",
            porqueEPerigoso = "O pH do bicarbonato é 9 (muito alcalino), enquanto a pele saudável tem pH ácido entre 4.7 e 5.5. Ele corrói a barreira de defesa natural, causando dermatite e ressecamento severo.",
            oQueFazerEmVezDisso = "Para esfoliar, use aveia em pó fina com água morna ou iogurte natural."
        ),
        SafetyAlert(
            tituloPerigo = "Pasta de Dentes em espinhas",
            ingredientesProibidos = "Dentífrico de qualquer marca",
            porqueEPerigoso = "A pasta contém mentol, flúor e detergentes feitos para o esmalte dentário. No rosto, irrita profundamente, queima a camada superficial e cria manchas escuras difíceis de sair.",
            oQueFazerEmVezDisso = "Use uma gotinha de loção de calamina ou uma compressa de chá verde gelado pontual."
        ),
        SafetyAlert(
            tituloPerigo = "Açúcar grosso esfregado com força na cara",
            ingredientesProibidos = "Açúcar refinado ou cristal diretamente no rosto",
            porqueEPerigoso = "Os grânulos de açúcar possuem bordas microscópicas cortantes que rasgam a pele invisivelmente (microlesões), rompendo vasinhos e facilitando bactérias da acne.",
            oQueFazerEmVezDisso = "Açúcar só deve ser usado em esfoliação labial ou nos calcanhares com óleo suave."
        )
    )

    val quandoProcurarMedico = listOf(
        "Espinhas nódulo-císticas: grandes elevações avermelhadas, muito profundas e dolorosas que não têm ponta amarela.",
        "Manchas suspeitas: manchas que mudam de cor, crescem rápido, têm bordas irregulares ou sangram.",
        "Feridas que não cicatrizam após 2 a 3 semanas.",
        "Descamação intensa com coceira severa, inchaço nos olhos ou lábios (sinal de alergia grave ou eczema).",
        "Pus abundante com febre ou calor local excessivo."
    )

    data class PhaseSkincare(
        val faseNome: String,
        val subtitulo: String,
        val caracteristicaPele: String,
        val dicasPraticas: List<String>
    )

    val pelePorFase = listOf(
        PhaseSkincare(
            faseNome = "Adolescência (12 a 19 anos)",
            subtitulo = "Explosão hormonal e glândulas sebáceas ativas",
            caracteristicaPele = "Aumento súbito de oleosidade na zona T (testa, nariz e queixo), cravos pretos e espinhas frequentes.",
            dicasPraticas = listOf(
                "Lave o rosto no máximo 2 vezes ao dia. Lavar demais causa efeito rebote (mais óleo).",
                "NUNCA esprema espinhas com as unhas para não deixar cicatrizes permanentes.",
                "Troque a fronha da almofada 2 vezes por semana.",
                "Não durma com maquiagem em hipótese alguma."
            )
        ),
        PhaseSkincare(
            faseNome = "Jovens Adultas (20 a 35 anos)",
            subtitulo = "Estresse, rotina corrida e prevenção de manchas",
            caracteristicaPele = "Surgimento de acne adulta na região da mandíbula/pescoço, olheiras por noites curtas e linhas de expressão iniciais.",
            dicasPraticas = listOf(
                "Protetor solar diário é o maior investimento anti-envelhecimento que existe.",
                "Beba no mínimo 2 litros de água para manter o viço natural.",
                "Priorize 7 a 8 horas de sono restaurador para diminuir cortisol.",
                "Use máscaras calmantes de babosa ou chá verde nas semanas de maior estresse."
            )
        ),
        PhaseSkincare(
            faseNome = "Pele Madura (35+ anos)",
            subtitulo = "Redução gradual de colágeno e hidratação natural",
            caracteristicaPele = "A pele tende a ficar mais seca, perde firmeza nas bochechas e marcas solares acumuladas aparecem.",
            dicasPraticas = listOf(
                "Evite sabonetes adstringentes fortes; prefira sabonetes em loção ou glicerina suave.",
                "Hidratação com cremes mais densos à noite após lavar o rosto.",
                "Compressas mornas seguidas de gel de babosa para estimular a circulação.",
                "Massagem facial ascendente suave ao aplicar os cremes."
            )
        ),
        PhaseSkincare(
            faseNome = "Gravidez & Amamentação",
            subtitulo = "Cuidado extra e segurança do bebé",
            caracteristicaPele = "Maior propensão a melasma (manchas escuras no rosto) por hormônios e maior sensibilidade.",
            dicasPraticas = listOf(
                "PROIBIDO: Ácido retinóico/retinol, hidroquinona e procedimentos químicos agressivos.",
                "SEGURO: Gel de babosa puro, água de arroz, aveia e hidratação neutra.",
                "Protetor solar mineral/físico com reaplicação a cada 3 horas para evitar melasma gravídico.",
                "Mantenha a pele muito bem hidratada para aliviar o ressecamento."
            )
        ),
        PhaseSkincare(
            faseNome = "Ciclo Menstrual (As 4 Semanas)",
            subtitulo = "Como as oscilações de estrogénio e progesterona afetam a pele",
            caracteristicaPele = "Semana 1 (Menstruação): pele sensível e seca. Semana 2: viço radiante. Semana 3: poros dilatados. Semana 4 (TPM): espinhas inflamatórias na mandíbula.",
            dicasPraticas = listOf(
                "Na semana anterior à menstruação (TPM): reduza frituras e use máscara de babosa para desinflamar.",
                "Durante a menstruação: foque em hidratação nutritiva e evite esfoliações agressivas.",
                "Na ovulação (meio do ciclo): aproveite o pico de viço natural da sua pele!",
                "Não entre em pânico com a espinha pré-menstrual: ela regride naturalmente com compressa de chá verde."
            )
        )
    )

    data class EducationalGuide(
        val topico: String,
        val pergunta: String,
        val explicacaoSimples: String,
        val mitoOuVerdade: String
    )

    val miniGuiaEducativo = listOf(
        EducationalGuide(
            topico = "O que é a Acne?",
            pergunta = "Por que surgem espinhas e cravos?",
            explicacaoSimples = "Cada poro da nossa pele tem uma pequena glândula que produz sebo para lubrificar. Quando o óleo se mistura com células mortas da pele, o canal entope. Se entrar ar, oxida e fica escuro (cravo preto). Se fechar e bactérias normais se proliferarem, vira uma espinha vermelha inflamada com pus.",
            mitoOuVerdade = "Mito comum: 'Lavar o rosto 10 vezes ao dia cura a acne.' FALSO! Lavar demais resseca a pele e o corpo produz o dobro de gordura para se proteger (efeito rebote)."
        ),
        EducationalGuide(
            topico = "Como Funciona o Protetor Solar?",
            pergunta = "Por que até em dias nublados e de chuva é preciso proteger?",
            explicacaoSimples = "O sol emite raios invisíveis UVB (que queimam e deixam a pele vermelha) e raios UVA (que atravessam nuvens e vidros da janela, quebrando as fibras de colágeno e provocando manchas escuras). O protetor solar cria um filtro que reflete ou absorve essa radiação prejudicial.",
            mitoOuVerdade = "Mito comum: 'Peles negras ou morenas não precisam de protetor solar.' FALSO! A melanina dá uma proteção natural leve, mas manchas escuras e envelhecimento precoce acontecem com força sem protetor."
        ),
        EducationalGuide(
            topico = "Pele Oleosa Precisa de Creme?",
            pergunta = "Hidratante não vai deixar a cara ainda mais gordurosa?",
            explicacaoSimples = "Óleo e água são coisas totalmente diferentes! Uma pele pode ter muito óleo na superfície e estar desidratada por dentro. Quando você não hidrata, o cérebro entende que a pele está desprotegida e manda produzir ainda mais gordura.",
            mitoOuVerdade = "Verdade comprovada: Usar um hidratante facial leve em gel ou loção líquida equilibra a pele e DIMINUI a oleosidade ao longo dos dias."
        )
    )
}
