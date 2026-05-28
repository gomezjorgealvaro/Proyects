getwd()

df <- read.csv("C:/Users/gomez/OneDrive/Documentos/heart_failure_clinical_records_dataset.csv")
head(df)
tail(df)

library(dplyr)

df <- df %>%
  mutate( DEATH_EVENT = factor(DEATH_EVENT, levels = c(0,1), labels = c("Vivo", "Fallecido")),
          anaemia = factor(anaemia),
          diabetes = factor(diabetes),
          high_blood_pressure = factor(high_blood_pressure),
          sex = factor(sex),
          smoking = factor(smoking)
          )

head(df)


df %>%
  summarise(n = n(),
            media_edad = mean(age, na.rm = TRUE),
            sd_edad = sd(age, na.rm = TRUE),
            
            media_ej = mean(ejection_fraction, na.rm = TRUE),
            sd_ej = sd(ejection_fraction, na.rm = TRUE),
            
            media_sc = mean(serum_creatinine, na.rm = TRUE),
            sd_sc = sd(serum_creatinine, na.rm = TRUE),
            
            media_sodio = mean(serum_sodium, na.rm = TRUE),
            sd_sodio = sd(serum_sodium, na.rm = TRUE),
            
            media_phos = mean(creatinine_phosphokinase, na.rm = TRUE),
            sd_phos = sd(creatinine_phosphokinase, na.rm = TRUE),
            
            media_plaq = mean(platelets, na.rm = TRUE),
            sd_plaq = sd(platelets, na.rm = TRUE),
            
            media_tiempo = mean(time, na.rm = TRUE),
            sd_tiempo = sd(time, na.rm = TRUE),
  )
tabla <- df %>%
  group_by(DEATH_EVENT)%>%
  summarise(n = n(),
            media_edad = mean(age, na.rm = TRUE),
            sd_edad = sd(age, na.rm = TRUE),
            
            media_ej = mean(ejection_fraction, na.rm = TRUE),
            sd_ej = sd(ejection_fraction, na.rm = TRUE),
            
            media_sc = mean(serum_creatinine, na.rm = TRUE),
            sd_sc = sd(serum_creatinine, na.rm = TRUE),
            
            media_sodio = mean(serum_sodium, na.rm = TRUE),
            sd_sodio = sd(serum_sodium, na.rm = TRUE),
            
            media_phos = mean(creatinine_phosphokinase, na.rm = TRUE),
            sd_phos = sd(creatinine_phosphokinase, na.rm = TRUE),
            
            media_plaq = mean(platelets, na.rm = TRUE),
            sd_plaq = sd(platelets, na.rm = TRUE),
            
            media_tiempo = mean(time, na.rm = TRUE),
            sd_tiempo = sd(time, na.rm = TRUE),
            )


library(janitor)


tabyl(df, anaemia, DEATH_EVENT) %>%
  adorn_percentages("col") %>%
  adorn_pct_formatting()

tabyl(df, diabetes, DEATH_EVENT) %>%
  adorn_percentages("col") %>%
  adorn_pct_formatting()

tabyl(df, high_blood_pressure, DEATH_EVENT) %>%
  adorn_percentages("col") %>%
  adorn_pct_formatting()

tabyl(df, sex, DEATH_EVENT) %>%
  adorn_percentages("col") %>%
  adorn_pct_formatting()

tabyl(df, smoking, DEATH_EVENT) %>%
  adorn_percentages("col") %>%
  adorn_pct_formatting()


library(gtsummary)

tabla <- df %>%
  tbl_summary(
    by = DEATH_EVENT,
    statistic = list(
      all_continuous() ~ "{mean} ({sd})",
      all_categorical() ~ "{n} ({p}%)"
    )
  ) %>%
  add_p()  # añade test estadísticos

tabla

library(ggplot2)

crear_barras <- function(x, breaks, labels) {
  cut(x, breaks = breaks, labels = labels, include.lowest = TRUE)
}

df$ef_bin <- crear_barras(df$ejection_fraction,
                        breaks = c(10,20,30,40,50,60,70,80),
                        labels = c("14-20","21-27","28-34","35-41",
                                   "42-48","49-55","56-62"))

ggplot(df, aes(x = ef_bin, fill = DEATH_EVENT)) +
  geom_bar(position = "dodge") +
  labs(title = "Fracción de eyección", x = "", y = "") +
  scale_fill_manual(values = c("#4EA8DE","#F94144")) +
  theme_dark()

df$creat_bin <- crear_barras(df$serum_creatinine,
                           breaks = c(0,1,1.5,2,3,4,5,10),
                           labels = c("0.5-1","1-1.5","1.5-2","2-3",
                                      "3-4","4-5",">5"))

ggplot(df, aes(x = creat_bin, fill = DEATH_EVENT)) +
  geom_bar(position = "dodge") +
  labs(title = "Creatinina sérica", x = "", y = "") +
  scale_fill_manual(values = c("#4EA8DE","#F94144")) +
  theme_dark()

df %>%
  filter(!is.na(age_bin)) %>%
  ggplot(aes(x = age_bin, fill = DEATH_EVENT)) +
  geom_bar(position = "dodge") +
  labs(title = "Edad", x = "", y = "") +
  scale_fill_manual(values = c("#4EA8DE","#F94144")) +
  theme_dark()

df$age_bin <- crear_barras(df$age,
                         breaks = c(40,50,60,70,80,90),
                         labels = c("40-50","50-60","60-70","70-80","80-90"))

ggplot(df, aes(x = age_bin, fill = DEATH_EVENT)) +
  geom_bar(position = "dodge") +
  labs(title = "Edad", x = "", y = "") +
  scale_fill_manual(values = c("#4EA8DE","#F94144")) +
  theme_dark()

df$sodium_bin <- crear_barras(df$serum_sodium,
                            breaks = c(110,120,130,135,140,145,150),
                            labels = c("110-120","120-130","130-135",
                                       "135-140","140-145","145-150"))

ggplot(df, aes(x = sodium_bin, fill = DEATH_EVENT)) +
  geom_bar(position = "dodge") +
  labs(title = "Sodio sérico", x = "", y = "") +
  scale_fill_manual(values = c("#4EA8DE","#F94144")) +
  theme_dark()

library(survival)
library(survminer)

df$DEATH_EVENT <- as.numeric(df$DEATH_EVENT)

surv_obj <- Surv(time = df$time, event = df$DEATH_EVENT)

fit <- survfit(surv_obj ~ 1, data = df, na.action = na.exclude)


ggsurvplot(fit,
           data = df,
           title = "Curva de supervivencia global",
           xlab = "Tiempo",
           ylab = "Supervivencia",
           pval = TRUE,
           conf.int = TRUE)

df$ef_group <- ifelse(df$ejection_fraction < 40, "Baja (<40)", "Normal (≥40)")

fit_ef <- survfit(Surv(time, DEATH_EVENT) ~ ef_group, data = df)

ggsurvplot(fit_ef,
           data = df,
           pval = TRUE,
           conf.int = TRUE,
           risk.table = TRUE,
           title = "Supervivencia según fracción de eyección",
           legend.title = "Grupo")

df$creat_group <- ifelse(df$serum_creatinine > 1.5, "Alta (>1.5)", "Normal")

fit_creat <- survfit(Surv(time, DEATH_EVENT) ~ creat_group, data = df)

ggsurvplot(fit_creat,
           data = df,
           pval = TRUE,
           conf.int = TRUE,
           risk.table = TRUE,
           title = "Supervivencia según creatinina")


df$age_group <- ifelse(df$age >= 65, "≥65", "<65")

fit_age <- survfit(Surv(time, DEATH_EVENT) ~ age_group, data = df)

ggsurvplot(fit_age,
           data = df,
           pval = TRUE,
           risk.table = TRUE,
           conf.int = TRUE,
           title = "Supervivencia según edad")

df$creat_group <- ifelse(df$serum_sodium > 135, "Normal", "Alta (>135)")

fit_creat <- survfit(Surv(time, DEATH_EVENT) ~ creat_group, data = df)

ggsurvplot(fit_creat,
           data = df,
           pval = TRUE,
           conf.int = TRUE,
           risk.table = TRUE,
           title = "Supervivencia según sodio")

df$anaemia <- as.numeric(as.character(df$anaemia))
df$diabetes <- as.numeric(as.character(df$diabetes))
df$high_blood_pressure <- as.numeric(as.character(df$high_blood_pressure))
df$smoking <- as.numeric(as.character(df$smoking))

df$comorbidity_count <- df$anaemia + df$diabetes + df$high_blood_pressure + df$smoking

df$comorbidity_group <- factor(df$comorbidity_count)

fit_comorb <- survfit(Surv(time, DEATH_EVENT) ~ comorbidity_group, data = df)

ggsurvplot(fit_comorb,
           data = df,
           pval = TRUE,
           risk.table = TRUE,
           title = "Supervivencia según número de comorbilidades",
           legend.title = "Nº comorbilidades")

df <- df %>%
  mutate(comorbidity_count = anaemia + diabetes + high_blood_pressure + smoking) %>%
  filter(comorbidity_count <= 3)

df$comorbidity_group <- factor(df$comorbidity_count,
                               levels = c(0,1,2,3),
                               labels = c("0","1","2","3"))

fit_comorb <- survfit(Surv(time, DEATH_EVENT) ~ comorbidity_group, data = df)

ggsurvplot(fit_comorb,
           data = df,
           pval = TRUE,
           risk.table = TRUE,
           conf.int = FALSE,
           palette = c("#E76F51","#E9C46A","#2A9D8F","#264653"),
           title = "Supervivencia según número de comorbilidades",
           legend.title = "Nº comorbilidades")

df_cox <- df %>%
  select(time, DEATH_EVENT, age, anaemia, creatinine_phosphokinase, 
         diabetes, ejection_fraction, high_blood_pressure, platelets, 
         serum_creatinine, serum_sodium, sex, smoking)

# 2. Generar la tabla de regresiones univariantes de Cox
tabla_cox_univariante <- df_cox %>%
  tbl_uvregression(
    method = coxph,                           # Especificamos el modelo de Cox
    y = Surv(time, DEATH_EVENT),              # Variable dependiente (Supervivencia)
    exponentiate = TRUE,                      # CRÍTICO: Exponenciar para obtener Hazard Ratios
    pvalue_fun = ~style_pvalue(.x, digits = 3)# Formato limpio para los p-valores
  ) %>%
  bold_labels() %>%                           # Poner en negrita los nombres de variables
  bold_p(t = 0.05) %>%                        # Resaltar en negrita los p-valores < 0.05
  modify_caption("**Análisis Univariante de Riesgos Proporcionales de Cox**")

# 3. Mostrar la tabla
tabla_cox_univariante


library(survival)
library(gtsummary)
library(dplyr)

modelo_completo <- coxph(
  Surv(time, DEATH_EVENT) ~ age + anaemia + creatinine_phosphokinase + 
    diabetes + ejection_fraction + high_blood_pressure + platelets + 
    serum_creatinine + serum_sodium + sex + smoking, 
  data = df
)

modelo_final <- step(modelo_completo, direction = "both", trace = 0)

summary(modelo_final)

library(survival)
library(survminer)

prueba_ph <- cox.zph(modelo_final)

print(prueba_ph)

graficos_schoenfeld <- ggcoxzph(prueba_ph)

print(graficos_schoenfeld)

coxph(Surv(time, DEATH_EVENT) ~ ejection_fraction + serum_creatinine + age + serum_sodium, data=df)
