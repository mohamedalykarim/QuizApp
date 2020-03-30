package mohalim.app.quizapp.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.app.quizapp.core.di.activities.feedback.FeedBackFragmentsModule;
import mohalim.app.quizapp.core.di.activities.feedback.FeedBackViewModelModule;
import mohalim.app.quizapp.core.di.activities.login.LoginFragmentModule;
import mohalim.app.quizapp.core.di.activities.login.LoginViewmodelModule;
import mohalim.app.quizapp.core.di.activities.admin_main.AdminMainFragmentsModule;
import mohalim.app.quizapp.core.di.activities.admin_main.AdminMainViewmodelModule;
import mohalim.app.quizapp.core.di.activities.questions.QuestionFragmentModule;
import mohalim.app.quizapp.core.di.activities.questions.QuestionViewModelModule;
import mohalim.app.quizapp.core.di.activities.quiz.QuizFragmentsModule;
import mohalim.app.quizapp.core.di.activities.quiz.QuizViewmodelModule;
import mohalim.app.quizapp.core.di.activities.result.ResultFragmentsModule;
import mohalim.app.quizapp.core.di.activities.result.ResultViewModelModule;
import mohalim.app.quizapp.core.di.activities.splash.SplashFragmentModule;
import mohalim.app.quizapp.core.di.activities.splash.SplashViewmodelModule;
import mohalim.app.quizapp.core.di.activities.statistics.StatisticsFragmentsModule;
import mohalim.app.quizapp.core.di.activities.statistics.StatisticsViewModelModule;
import mohalim.app.quizapp.core.di.activities.user_main.UserMainFragmentsModules;
import mohalim.app.quizapp.core.di.activities.user_main.UserMainViewModelsModule;
import mohalim.app.quizapp.ui.feedback.FeedBackActivity;
import mohalim.app.quizapp.ui.login.LoginActivity;
import mohalim.app.quizapp.ui.admin_main.AdminMainActivity;
import mohalim.app.quizapp.ui.questions.QuestionsActivity;
import mohalim.app.quizapp.ui.quiz.QuizActivity;
import mohalim.app.quizapp.ui.result.ResultActivity;
import mohalim.app.quizapp.ui.splash.SplashActivity;
import mohalim.app.quizapp.ui.statistics.StatisticsActivity;
import mohalim.app.quizapp.ui.user_main.UserMainActivity;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector( modules = { SplashViewmodelModule.class, SplashFragmentModule.class})
    abstract SplashActivity provideSplashActivity();

    @ContributesAndroidInjector(modules = {LoginFragmentModule.class, LoginViewmodelModule.class})
    abstract LoginActivity provideLoginActivity();

    @ContributesAndroidInjector(modules = {AdminMainFragmentsModule.class, AdminMainViewmodelModule.class})
    abstract AdminMainActivity provideAdminMainActivity();

    @ContributesAndroidInjector(modules = {UserMainFragmentsModules.class, UserMainViewModelsModule.class})
    abstract UserMainActivity contributeUserMainActivity();

    @ContributesAndroidInjector(modules = { QuestionFragmentModule.class, QuestionViewModelModule.class})
    abstract QuestionsActivity contributeQuestionActivity();

    @ContributesAndroidInjector(modules = {QuizFragmentsModule.class, QuizViewmodelModule.class})
    abstract QuizActivity contributeQuizActivity();

    @ContributesAndroidInjector(modules = {ResultFragmentsModule.class, ResultViewModelModule.class})
    abstract ResultActivity contributeResultActivity();

    @ContributesAndroidInjector(modules = {FeedBackFragmentsModule.class, FeedBackViewModelModule.class})
    abstract FeedBackActivity contributeFeedBackActivity();

    @ContributesAndroidInjector(modules = {StatisticsFragmentsModule.class, StatisticsViewModelModule.class})
    abstract StatisticsActivity contributeStatisticsActivity();

}
