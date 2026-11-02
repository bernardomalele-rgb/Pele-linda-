package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.FavoriteRecipeEntity
import com.example.data.local.PeleLindaDatabase
import com.example.data.local.RoutineHistoryEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.AccountStatus
import com.example.data.model.PaymentStatus
import com.example.data.repository.PeleLindaRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context matches Pele Linda`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Pele Linda", appName)
  }

  @Test
  fun `repository registration and 7-day trial flow`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = PeleLindaRepository(context)

    val registered = repo.registerUser(
      nome = "Maria Santos",
      idade = 24,
      telemovel = "923112233",
      termosAceites = true
    )
    assertTrue(registered)

    val user = repo.userProfile.value
    assertEquals("Maria Santos", user.nome)
    assertEquals(24, user.idade)
    assertEquals(AccountStatus.EM_TESTE, user.estadoConta)
    assertTrue(repo.getDaysRemaining() >= 6)
  }

  @Test
  fun `multicaixa payment submission and admin approval flow`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = PeleLindaRepository(context)

    repo.registerUser("Teresa Bernardo", 28, "923471505", true)

    val result = repo.submitPayment(
      dataPagamento = "02/11/2026",
      metodo = "Multicaixa Express (923471505)",
      referencia = "923471505",
      comprovativoUri = "content://media/proof_sample_1.jpg"
    )
    assertTrue(result.isSuccess)
    val payment = result.getOrThrow()
    assertEquals(PaymentStatus.PENDENTE, payment.estado)
    assertEquals(3500, payment.valorKz)

    val approved = repo.adminApprovePayment(payment.id)
    assertTrue(approved)

    val updatedUser = repo.userProfile.value
    assertEquals(AccountStatus.ATIVA, updatedUser.estadoConta)
  }

  @Test
  fun `room database UserProfileDao offline persistence`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = PeleLindaDatabase.getInstance(context)
    val dao = db.userProfileDao()

    val profile = UserProfileEntity(
      id = "primary_user",
      nome = "Ana Paula",
      idade = 26,
      telemovel = "924889900",
      tipoPele = "Oleosa",
      isVerified = true
    )
    dao.insertOrUpdateProfile(profile)

    val retrieved = dao.getUserProfile()
    assertNotNull(retrieved)
    assertEquals("Ana Paula", retrieved?.nome)
    assertEquals("Oleosa", retrieved?.tipoPele)
  }

  @Test
  fun `room database FavoriteRecipeDao add and remove`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = PeleLindaDatabase.getInstance(context)
    val dao = db.favoriteRecipeDao()

    val fav = FavoriteRecipeEntity(
      recipeId = "rec_babosa_mel",
      title = "Máscara de Babosa",
      subtitle = "Calmante natural",
      budgetRange = "Até 500 Kz",
      estimatedCostKz = 450,
      frequency = "2x por semana",
      recommendedSkinType = "Oleosa",
      mainBenefit = "Anti-inflamatório"
    )

    dao.addFavorite(fav)
    assertTrue(dao.isFavorite("rec_babosa_mel"))

    dao.removeFavorite("rec_babosa_mel")
    assertFalse(dao.isFavorite("rec_babosa_mel"))
  }

  @Test
  fun `room database RoutineHistoryDao record and query`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = PeleLindaDatabase.getInstance(context)
    val dao = db.routineHistoryDao()

    val history = RoutineHistoryEntity(
      dateString = "02/11/2026",
      taskId = "m1",
      taskTitle = "Limpeza Fresca",
      period = "MANHA",
      stepNumber = 1,
      completed = true
    )

    dao.insertHistory(history)
    // Delete for today
    dao.deleteHistoryItem("02/11/2026", "m1")
  }

  @Test
  fun `admin authentication with password 2012 and exact error on failure`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val vm = com.example.ui.viewmodel.PeleLindaViewModel(app)

    // Test wrong password
    vm.adminPinInput.value = "9999"
    vm.loginAdmin()
    assertFalse(vm.isAdminAuthenticated.value)
    assertEquals("A senha está incorreta.", vm.adminAuthError.value)

    // Test correct password 2012
    vm.adminPinInput.value = "2012"
    vm.loginAdmin()
    assertTrue(vm.isAdminAuthenticated.value)
  }
}
