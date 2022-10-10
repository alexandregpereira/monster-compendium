package br.alexandregpereira.hunter.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import br.alexandregpereira.hunter.data.AppDatabase
import br.alexandregpereira.hunter.data.monster.folder.local.dao.MonsterFolderDao
import br.alexandregpereira.hunter.data.monster.local.dao.AbilityScoreDao
import br.alexandregpereira.hunter.data.monster.local.dao.ActionDao
import br.alexandregpereira.hunter.data.monster.local.dao.ConditionDao
import br.alexandregpereira.hunter.data.monster.local.dao.DamageDao
import br.alexandregpereira.hunter.data.monster.local.dao.DamageDiceDao
import br.alexandregpereira.hunter.data.monster.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.monster.local.dao.ReactionDao
import br.alexandregpereira.hunter.data.monster.local.dao.SavingThrowDao
import br.alexandregpereira.hunter.data.monster.local.dao.SkillDao
import br.alexandregpereira.hunter.data.monster.local.dao.SpecialAbilityDao
import br.alexandregpereira.hunter.data.monster.local.dao.SpeedDao
import br.alexandregpereira.hunter.data.monster.local.dao.SpeedValueDao
import br.alexandregpereira.hunter.data.spell.local.dao.SpellDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    internal fun provideAppDataBase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "hunter-database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    internal fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    @Provides
    internal fun provideAbilityScoreDao(appDatabase: AppDatabase): AbilityScoreDao {
        return appDatabase.abilityScoreDao()
    }

    @Provides
    internal fun provideActionDao(appDatabase: AppDatabase): ActionDao {
        return appDatabase.actionDao()
    }

    @Provides
    internal fun provideConditionDao(appDatabase: AppDatabase): ConditionDao {
        return appDatabase.conditionDao()
    }

    @Provides
    internal fun provideDamageDao(appDatabase: AppDatabase): DamageDao {
        return appDatabase.damageDao()
    }

    @Provides
    internal fun provideDamageDiceDao(appDatabase: AppDatabase): DamageDiceDao {
        return appDatabase.damageDiceDao()
    }

    @Provides
    internal fun provideMonsterDao(appDatabase: AppDatabase): MonsterDao {
        return appDatabase.monsterDao()
    }

    @Provides
    internal fun provideMonsterFolderDao(appDatabase: AppDatabase): MonsterFolderDao {
        return appDatabase.monsterFolderDao()
    }

    @Provides
    internal fun provideSavingThrowDao(appDatabase: AppDatabase): SavingThrowDao {
        return appDatabase.savingThrowDao()
    }

    @Provides
    internal fun provideSkillDao(appDatabase: AppDatabase): SkillDao {
        return appDatabase.skillDao()
    }

    @Provides
    internal fun provideSpecialAbilityDao(appDatabase: AppDatabase): SpecialAbilityDao {
        return appDatabase.specialAbilityDao()
    }

    @Provides
    internal fun provideSpeedDao(appDatabase: AppDatabase): SpeedDao {
        return appDatabase.speedDao()
    }

    @Provides
    internal fun provideSpeedValueDao(appDatabase: AppDatabase): SpeedValueDao {
        return appDatabase.speedValueDao()
    }

    @Provides
    internal fun provideReactionDao(appDatabase: AppDatabase): ReactionDao {
        return appDatabase.reactionDao()
    }

    @Provides
    internal fun provideSpellDao(appDatabase: AppDatabase): SpellDao {
        return appDatabase.spellDao()
    }
}
