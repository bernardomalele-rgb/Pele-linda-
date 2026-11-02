package com.example.ui.screens.recipes

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BudgetRange
import com.example.data.model.Recipe
import com.example.data.repository.SkincareData
import com.example.ui.components.BigButton
import com.example.ui.components.PeleLindaTopBar
import com.example.ui.theme.*
import com.example.ui.viewmodel.PeleLindaViewModel

@Composable
fun RecipesScreen(viewModel: PeleLindaViewModel) {
    val selectedFilter by viewModel.selectedBudgetFilter.collectAsState()
    val selectedRecipe by viewModel.selectedRecipe.collectAsState()
    val allergyStartTime by viewModel.allergyTestStartTime.collectAsState()
    val allergyRecipeName by viewModel.allergyRecipeName.collectAsState()
    val favoriteEntities by viewModel.favoriteRecipes.collectAsState()

    var showOnlyFavorites by remember { mutableStateOf(false) }
    var showAllergyModal by remember { mutableStateOf(false) }

    val favoriteIds = remember(favoriteEntities) { favoriteEntities.map { it.recipeId }.toSet() }

    val filteredRecipes = remember(selectedFilter, showOnlyFavorites, favoriteIds) {
        var list = SkincareData.receitasEconomicas
        if (showOnlyFavorites) {
            list = list.filter { favoriteIds.contains(it.id) }
        }
        if (selectedFilter != null) {
            list = list.filter { it.categoriaPreco == selectedFilter }
        }
        list
    }

    Scaffold(
        topBar = {
            PeleLindaTopBar(
                title = "Soluções Caseiras",
                subtitle = "Receitas baratas com ingredientes naturais"
            )
        },
        containerColor = CreamBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Natural Ingredients Banner
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.natural_recipes),
                            contentDescription = "Ingredientes naturais de skincare",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.35f))
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Surface(
                                color = MintContainer,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "100% Económico",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MintOnContainer
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Babosa, Mel, Chá Verde e Aveia",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // Allergy Test Active Warning or Quick Guide Trigger
            item {
                val isTestRunning = allergyStartTime > 0L
                val hoursElapsed = if (isTestRunning) {
                    val diff = System.currentTimeMillis() - allergyStartTime
                    (diff / (1000 * 60 * 60)).toInt()
                } else 0

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isTestRunning) MintContainer else RoseContainer
                    ),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAllergyModal = true }
                        .testTag("allergy_test_card")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isTestRunning) Icons.Default.HourglassTop else Icons.Default.Science,
                                contentDescription = null,
                                tint = if (isTestRunning) MintSecondary else RosePrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isTestRunning) "Teste de Alergia em Andamento (24h)" else "Teste de Alergia Guiado de 24h",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTestRunning) MintOnContainer else RoseOnContainer
                                )
                            )
                            Text(
                                text = if (isTestRunning) "Receita: $allergyRecipeName ($hoursElapsed/24h) - Toque para verificar" else "Faça sempre o teste no antebraço antes de usar qualquer receita nova.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isTestRunning) MintOnContainer.copy(alpha = 0.8f) else RoseOnContainer.copy(alpha = 0.8f)
                                )
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = if (isTestRunning) MintSecondary else RosePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Budget Filters Header & Horizontal Chips
            item {
                Column {
                    Text(
                        text = "Filtrar por Orçamento:",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterChip(
                                selected = !showOnlyFavorites && selectedFilter == null,
                                onClick = {
                                    showOnlyFavorites = false
                                    viewModel.selectedBudgetFilter.value = null
                                },
                                label = { Text("Todas") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RosePrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                        item {
                            FilterChip(
                                selected = showOnlyFavorites,
                                onClick = { showOnlyFavorites = !showOnlyFavorites },
                                label = { Text("Favoritas ❤️ (${favoriteEntities.size})") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RosePrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                        items(BudgetRange.values()) { range ->
                            FilterChip(
                                selected = !showOnlyFavorites && selectedFilter == range,
                                onClick = {
                                    showOnlyFavorites = false
                                    viewModel.selectedBudgetFilter.value = range
                                },
                                label = { Text(range.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RosePrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // List of Recipes
            items(filteredRecipes) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    isFavorite = favoriteIds.contains(recipe.id),
                    onToggleFavorite = { viewModel.toggleFavoriteRecipe(recipe) },
                    onClick = { viewModel.selectedRecipe.value = recipe },
                    onAddShoppingList = {
                        viewModel.repository.addIngredientsToShoppingList(recipe)
                        viewModel.showSnackbar("Ingredientes adicionados à sua Lista de Compras!")
                    }
                )
            }
        }
    }

    // Modal Sheet for Recipe Detail
    if (selectedRecipe != null) {
        val recipe = selectedRecipe!!
        AlertDialog(
            onDismissRequest = { viewModel.selectedRecipe.value = null },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.repository.addIngredientsToShoppingList(recipe)
                        viewModel.showSnackbar("Ingredientes adicionados à Lista de Compras!")
                        viewModel.selectedRecipe.value = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Adicionar à Lista de Compras")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.selectedRecipe.value = null }) {
                    Text("Fechar", color = TextDark)
                }
            },
            title = {
                Text(
                    text = recipe.titulo,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = RosePrimary,
                        fontSize = 20.sp
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                        .verticalScroll(androidx.compose.foundation.rememberScrollState())
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            color = MintContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Custo Estimado: ~${recipe.custoEstimadoKz} Kz",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MintOnContainer
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            color = CreamSurfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = recipe.frequencia,
                                style = MaterialTheme.typography.labelSmall.copy(color = TextDark),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Indicado para: ${recipe.tipoPeleRecomendada}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextDark
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Ingredientes Necessários:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextDark)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    recipe.ingredientes.forEach { ing ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            Text("• ${ing.nome} (${ing.quantidade})", style = MaterialTheme.typography.bodySmall)
                            Text("~${ing.precoEstimadoKz} Kz", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = RosePrimary))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Passo a Passo:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextDark)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    recipe.passoAPasso.forEachIndexed { index, stepText ->
                        Row(modifier = Modifier.padding(vertical = 3.dp)) {
                            Text("${index + 1}. ", fontWeight = FontWeight.Bold, color = RosePrimary, style = MaterialTheme.typography.bodySmall)
                            Text(stepText, style = MaterialTheme.typography.bodySmall, color = TextDark)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Safety alert for recipe
                    Card(
                        colors = CardDefaults.cardColors(containerColor = HoneyContainer),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = HoneyTertiary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = recipe.avisosSeguranca,
                                style = MaterialTheme.typography.bodySmall.copy(color = HoneyOnContainer, fontSize = 11.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Start allergy test button
                    OutlinedButton(
                        onClick = {
                            viewModel.startAllergyTestForRecipe(recipe)
                            viewModel.selectedRecipe.value = null
                            showAllergyModal = true
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Iniciar Teste de Alergia de 24h para esta receita")
                    }
                }
            }
        )
    }

    // Modal for 24h Allergy Test Guide
    if (showAllergyModal) {
        AlertDialog(
            onDismissRequest = { showAllergyModal = false },
            confirmButton = {
                Button(
                    onClick = { showAllergyModal = false },
                    colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
                ) {
                    Text("Entendi, vou observar")
                }
            },
            dismissButton = {
                if (allergyStartTime > 0L) {
                    TextButton(onClick = {
                        viewModel.resetAllergyTest()
                        showAllergyModal = false
                    }) {
                        Text("Concluir / Cancelar Teste", color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            title = {
                Text(
                    "Teste de Alergia Guiado (24h)",
                    fontWeight = FontWeight.Bold,
                    color = RosePrimary
                )
            },
            text = {
                Column(modifier = Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState())) {
                    Text(
                        text = "Antes de aplicar qualquer máscara ou produto novo no rosto, proteja a sua pele com este teste simples de 24 horas:",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextDark)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    AllergyStep(1, "Lave e seque uma pequena área no antebraço ou atrás da orelha.")
                    AllergyStep(2, "Aplique uma pequena gota da receita (tamanho de uma moeda de 10 Kz).")
                    AllergyStep(3, "Deixe repousar por 24 horas sem coçar ou esfregar.")
                    AllergyStep(4, "Se após 24h a pele estiver normal (sem coceira, ardor ou vermelhidão), o uso no rosto é seguro!")

                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = RoseContainer),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Aviso: Se sentir coceira, calor ou ardor a qualquer momento, lave imediatamente com água fresca e sabonete neutro e NÃO use essa receita.",
                            style = MaterialTheme.typography.bodySmall.copy(color = RoseOnContainer),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    onAddShoppingList: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(OutlineSoft)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("recipe_card_${recipe.id}")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = MintContainer,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "${recipe.categoriaPreco.label} (~${recipe.custoEstimadoKz} Kz)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MintOnContainer
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = recipe.tempoPreparo,
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = recipe.titulo,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    fontSize = 17.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = recipe.subTitulo,
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${recipe.ingredientes.size} ingredientes simples",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = RosePrimary
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("recipe_favorite_button_${recipe.id}")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remover dos favoritos" else "Adicionar aos favoritos",
                            tint = if (isFavorite) RosePrimary else TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = onAddShoppingList,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = "Adicionar ingredientes à lista",
                            tint = RosePrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AllergyStep(num: Int, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(MintSecondary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$num",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall, color = TextDark)
    }
}
