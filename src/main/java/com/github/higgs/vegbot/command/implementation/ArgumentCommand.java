package com.github.higgs.vegbot.command.implementation;

import com.github.higgs.vegbot.command.CommandContext;
import com.github.higgs.vegbot.command.CommandExecutor;
import com.github.higgs.vegbot.command.CommandResult;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.List;
import java.util.*;

public class ArgumentCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandContext context) {
        Optional<String> argument = context.requestArgument("argument");

        if(argument.isPresent()) {
             List<String> message = arguments.getOrDefault(argument.get().toUpperCase(Locale.ROOT), Collections.singletonList("That is not a listed argument. Please pick from the list found at ?fallacy"));
             EmbedBuilder embed = new EmbedBuilder()
                     .setTitle("📚 Your Anti-Vegan Argument 📚")
                     .setColor(Color.CYAN);
            for(String field : message) {
                embed.addField("", field, false);
            }
            String picture = pictures.getOrDefault(argument.get().toUpperCase(Locale.ROOT), null);
            if(picture != null) {
                embed.setImage(picture);
            }
            context.getChannel().sendMessageEmbeds(embed.build()).queue();
        } else {
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("📚 Anti-Vegan Argument List 📚")
                    .addField("",
                            " • `ancestors`  -  We've always done it like that!\n" +
                                    " • `bad_vegan`  -  TheVeganTeacher and PETA are bad!\n" +
                                    " • `economy`  -  Farmers will lose their jobs!\n" +
                                    " • `estrogen`  -  Plant foods like soy have estrogen!\n" +
                                    " • `food_chain`  -  Eating other animals is just part of the food chain!\n" +
                                    " • `humane`  -  Humane meat is totally fine!\n" +
                                    " • `humans_first`  -  We should put people's welfare first!\n" +
                                    " • `hunting`  -  Hunting is actually good for the environment!\n" +
                                    " • `nirvana`  -  It will never be perfect!\n" +
                                    " • `omega_3`  -  You need omega-3s from fish!\n" +
                                    " • `overpopulation`  -  If we don't eat meat, there will be overpopulation!\n" +
                                    " • `pesticides`  -  Veganism requires excessive pesticide use!\n" +
                                    " • `plant_feelings`  -  Plants have feelings, right?\n" +
                                    " • `predators`  -  If a lion does it, I can too!\n" +
                                    " • `price`  -  I don't have the money to go vegan!\n" +
                                    " • `privilege`  -  Veganism is for privileged people!\n" +
                                    " • `protein`  -  Meat is the only source of reliable protein!\n" +
                                    " • `taste`  -  I like the taste though!"
                            ,false)
                    .setColor(Color.CYAN)
                    .build();
            context.getChannel().sendMessageEmbeds(embed).queue();

        }
        return CommandResult.empty();
    }

    private static final Map<String, List<String>> arguments = new HashMap<>();
    private static final Map<String, String> pictures = new HashMap<>();

    private static final String PLANT_FEELINGS =
            "One common critique of veganism is that vegans are actually hurting more creatures because they eat plants " +
            "which in themselves have feelings. There are two issues with this statement:\n\n" +
            "**1.** Plants and animals are fundamentally different in their ability to process information intelligently. A plant is not " +
            "capable of having feelings.";

    private static final String PLANT_FEELINGS_2 =
            "**2.** If you were to take the position that plants should receive equal moral value, you would first need to understand that by eating " +
            "meat, *you kill significantly more plants in the agricultural process*. Because each animal a meat eater would kill needs to maintain " +
            "their bodily function like temperature, immune system, mating, and more, they need to consume more plant matter than a human would. In fact, " +
            "if the citizens of the United States alone would switch to a plant based diet, the country could feed an additional 350,000,000 Americans with the food that is " +
            "currently being used to feed livestock animals. That means eliminating animals being killed for food *and* far less plants being killed to feed said animals.";

    private static final String NIRVANA =
            "It is easy say \"well, we will never save all animals so while bother?\" However, this presents an easy to spot \"nirvana fallacy\". This fallacy " +
                    "states can be seen in the argument as we are artificially showcasing only two options in a situation when in actuality, there are other choices. With animal ethics, " +
                    "we know that we do not have to pick between sentencing trillions of animals to death and saving each one. We can make minor changes to affect the lives of a significant chunk " +
                    "of the animal population we harm.";

    private static final String NIRVANA_2 =
            "To better understand the issue with this argument, take an example most people today understand to be an important issue of concern like racism. It is easy to see that " +
            "completely stopping all instances of racism is near impossible. However, that does not mean we should all openly be racist or even just permit others to do so. Using this logic, " +
            "we can see why using this argument against choosing to opt for cruelty free options is fallacious.";

    private static final String FOOD_CHAIN =
            "Some will argue because humans are at the top of an evolutionary food chain, we are morally right in doing what we want to \"lower\" animals. While it may be true that " +
            "we *could* do what we want, it does not mean we *should*. For example, we all have the power to legally adopt and kill sixty dogs in one sitting. Dogs are generally far too slow and weak " +
            "to be able to escape from an attacker. However, a person's ability to kill sixty dogs does not mean they are morally just in doing so. We can consider these situations equal as " +
            "killing pigs, chickens, and cows is equally as unnecessary and cruel considering each animal has the same ability to suffer and we can simply get food from plant-based sources. ";

    private static final String FOOD_CHAIN_2 =
            "Additionally, to say humans are helping the food chain isn't entirely correct. To begin, the \"food chain\" is much more akin to a " +
            "\"food web\" wherein each creature impacts others through a massive web of compounding effects. Humans, have a considerable negative " +
            "effect on this web. Considering greenhouse gases promoting natural disasters and climate change, plastic waste (mostly from fishing lines) " +
            "flooding our oceans, and mass slaughter of confined animals, the effect humans have on our fellow animals is very much not \"natural\" and an " +
            "overall extreme detriment.";

    private static final String PRICE =
            "Something most everyone is concerned with is the price of basic living costs. Which is why people will often say the price " +
            "of a plant-based diet is keeping them from pursuing veganism. However, this is not an issue as the plant based diet as been " +
            "shown to actually be cheaper than most other diets with the pescatarian diet being the most costly. While this may seem surprising, " +
            "this is easy to understand when you see that the staples to a plant based diets are often the cheapest foods in the grocery store like " +
            "pasta, rice, oats, legumes (soy, beans, peas, lentils), bread, and vegetables. Even formerly luxury items like plant-based meats are now often cheaper " +
            "than flesh-foods because of the high demand.";

    private static final String PESTICIDES = "A reasonable concern with modern agriculture is the use of excessive and irresponsible pesticide use. Some critics " +
            "of veganism will point out that eating plants will require more pesticides. However, the opposite is actually true. Since eating animals requires more crops " +
            "to be grown in order to feed the animals, more pesticides must be used than if we simply used the plant food for ourselves. With this logic, going vegan is a " +
            "good step forward to reduce our use of pesticides and other harmful chemicals.";

    private static final String PROTEIN =
            "Protein, one of three main macronutrients humans require to survive, is often mentioned as a reason not to go vegan. This misguided belief originates in the idea " +
                "meat and other animal products are the only reliable sources of protein. However, this is not true in the slightest. Proteins are found in varying amounts in all " +
                "foods. However, the plant-based foods that are highest in protein are foods such as grains, legumes, leafy greens, seeds, nuts, particular fruits like avocados, mock meats, protein powders/bars " +
                "and even plant based milks. Each of these, especially in combination, are perfectly adequate for all lifestyles including athletes. Simple meals such as spaghetti and (mock) meat sauce with " +
                "garlic bread can amount to enough protein for an entire day (~55g)";

    private static final String PROTEIN_2 =
            "Another concern that is often referenced is the amino acid profile and digestibility of plant based protein sources. The amino acid profile is quite easy to examine " +
            "by noticing that each plant based protein source has varying levels of each amino acid. If you consume more than the necessary amount, it is likely you will get enough of each " +
            "amino acid. Additionally, simply consuming a variety of protein sources will result in a higher net amount of each amino acid. Digestibility, on the other hand, " +
            "is mostly an issue regarding some of the misinterpretations of modern science. Older digestibility scores often missed out on several important factors " +
            "between animal and plant proteins which threw off statistics. Modern standards now show common protein sources like tofu have a fantastic 90+% digestibility score.";

    private static final String TASTE =
            "A the end of the day, it's the easiest option to simply say that eating meat is a personal choice because you enjoy the taste. This argument relies heavily on idea that personal desire " +
            "outweighs suffering even if the benefit to you is small and the suffering is great. As an example of this logic placed elsewhere, one could argue that it is perfectly reasonable " +
            "to kill 100 dogs because it makes you feel good. This, for most, is clearly unethical.";

    private static final String TASTE_2 =
            "Compared to killing animals for food, we can note the similarities in the situations. " +
            "In the first, we see immense harm in that 10,000+ animals per person are killed (based on averages) because the killer gets sensory pleasure from their flesh. " +
            "This keeps in mind that the harm can be easily avoided by simply eating a plant based diet. " +
            "In our example with dogs, only 100 dogs are killed (1/100 the harm) for the same reason in that it gives the person sensory pleasure. In the same way, this can be completely avoided simply by choosing " +
            "to avoid killing the dogs in the example.";

    private static final String ANCESTORS =
            "Much of the way humans perceive culture and ethics at a quick glance is by looking at what people did in the past. The basic thought pattern that stems from this belief is that since we did it in the past, it must be fine for us to do it today. " +
            "Which is why it is understandable people jump to this idea when discussing animal agriculture. Though, it is important to look deeper into this idea before drawing conclusions. " +
            "To help us understand, let's use a different situation with the same logic. We can see that human history is filled with awful practices. Homophobia is the norm for much of the current older " +
            "generation, racism was acceptable as little as 70 years ago, and slavery the same only 200 years ago. With these practices in mind, if we use the same argument that it must be fine if we did it in the past, each of these " +
            "practices should still be acceptable today. For this reason, we ought to form logical ethical decisions about animal ethics instead of copying ancestral ideals.";

    private static final String OVERPOPULATION =
            "Practically speaking, a common concern with everyone going vegan is what we would do with all of the animals that are no longer going to be killed. This argument unfortunately relies upon the unrealistic " +
            "idea that each and every person in the world would turn vegan over night thus creating a massive overpopulation issue. This is especially exacerbated by the idea that animal agriculture wherein animals" +
            "are born, raised, bred, and killed in one controlled environment is equivalent to natural situations. ";

    private static final String OVERPOPULATION_2 =
            "However, these conditions are not reality. Instead, the shift to veganism will happen as " +
            "public morals shift, legislation is passed, and commercial success for plant-based products rises. If the demand for animal products drops, the companies that produce products made from animal flesh, eggs, and milk will " +
            "also produce less supply by limiting or stopping the breeding of animals thus reducing the amount of animals on farms. If we stop the breeding of animals, the only animals left to worry about " +
            "overpopulating the earth are the ones that are still being sought after for meat. With this understanding, we also have to note that the longer living " +
            "animals humans commonly kill for food such as cows only ever live to a maximum of roughly 6 years on a farm. This leaves the remaining \"supply\" of animals to either be killed by the last meat eaters or sent to sanctuaries. ";

    private static final String OVERPOPULATION_3 =
            "It should be noted that this does not necessitate the extinction of cows, pigs, chickens etc. Those animals lived in the wild before humans arrived and still do to this day with the added " +
            "benefit of not causing issues with overpopulation.";

    private static final String ESTROGEN =
            "A common concern with health on a plant based diet is exogenous (externally sourced) estrogens in foods. This concern usually targets soy, but questions are often posed to other foods like alcohol and flax seeds. " +
            "What people are really talking about with this situation is a class of plant hormones called phytoestrogens, or more specifically for our discussion, isoflavones. Isoflavones, while somewhat mimicking the general molecular structure of estrogen, are nowhere " +
            "near as potent with some of isoflavones actually preventing other estrogens from properly binding. ";

    private static final String ESTROGEN_2 =
            "Another point of interest with plant-based diets and sex hormone production is that the vast quantity of peer reviewed studies show a plant based diet does not show any uptick in estrogen production nor " +
            "does it impair testosterone production. This, paired with common plant based proteins like soy being a fantastic source of antioxidants, reveal why large health organizations like Harvard Health suggest plant based proteins " +
            "as a great way to improve public health.";

    private static final String CULTURE =
            "When confronted with an ethical issue, an immediate out is to point to others in one's culture to show that other people are doing it so it must, by proxy, be morally just. This same argument " +
            "is used to fight for the ability to kill animals. If one's culture allows you to kill animals, it must be morally just to do so. Following this logic, we can find many instances in our history " +
            "where other people used this same argument. In similarity to current animal rights, several countries previously had festivals involving the killing and eating of dogs. Many of these practices are " +
            "now banned, but the people at the time would tell you simply that it was their culture and therefore it was okay. You can also find this in more extreme examples like America's south during the civil war " +
            "claiming owning other people as slaves was simply a part of their culture. If we find issue with the justification for these actions, it seems illogical to use it for ethics concerning other animals.";

    private static final String HUNTING =
            "The claim that hunting is actually a good thing for the environment is often made in response to veganism. The basic reasoning behind the claim is that natural wildlife populations get into situations wherein" +
            "their population is so large that they wreak havoc on their environment. There is two major issues with this argument:";

    private static final String HUNTING_2 =
            "**1**. Most people do not hunt for their food. Considering a vast majority of people live in urban areas with most of the remaining population living in suburbs near metropolitan areas, most of the flesh-foods " +
            "purchased are at grocery stores. Not only would hunting for flesh be infeasible because of the lack of wildlife in the area, but also because if every person were to hunt for their food, species of wild animals would quickly " +
            "die out considering humans kill roughly 3,000,000,000,000 animals annually; most of which are farmed animals like farmed fish, chickens, cows, and pigs. ";

    private static final String HUNTING_3 =
            "**2**. Additionally, this critique is using a blatant false dichotomy. The idea that you either have wildlife overpopulation *or* humans hunt animals is simply false. If humans want to control wildlife populations, " +
            "there are reasonable options to do so without cruelty. For one, since humans have such a negative impact on the natural habitat for animals which often forces them into unnatural situations that result in overpopulation, " +
            "the first fix should always be to fix the root cause by limiting our impact on the environment. For any cases that escape this solution, dart-based birth control is a decent alternative. Not only is this scalable, but it " +
            "limits overpopulation while also giving former hunters a hobby that reduces suffering instead of creating it. ";

    private static final String HUMANE_MEAT =
            "While most agree that factory farming is fundamentally unethical, there are still many who argue that \"humane\" meat is perfectly fine. Usually this means that the animal ate what they normally " +
                    "eat in the wild, live a bit longer, and are killed quickly. While this might seem to appease the guilt of killing animals, the issue is actually a fair bit more complicated.";

    private static final String HUMANE_MEAT_2 =
            "One of the first issues with the idea of \"humane meat\" is the dilemma of how one can kill something that does not want to be killed humanely. To be humane, an action has to be characterized by kindness, mercy, and compassion. " +
            "In stark contrast to each of these qualities, killing someone who clearly does not want to die clearly does not fit the definition of humane. While euthanasia has decent reason to be moral, killing for flesh " +
            "as if the animal was an object to be used most certainly is not the same.";

    private static final String HUMANE_MEAT_3 =
            "Another issue with the idea of \"humane \" meat is the fallacious idea that local farms are somehow more ethical than farms far away. Regardless of where the killing occurs, someone has to kill another sentient being. This is compounded by the belief " +
            "that local farms are more ethical because animals eat more grass. Though, even grass-fed animals are still fed a significant portion of their diet from roughage and other food sources. In fact, " +
            "many legumes like soy are considered part of the \"grass-fed\" diet by agricultural organizations like USDA. ";

    private static final String HUMANE_MEAT_4 =
            "Finally, we have to understand that more often than not, the more humane something is, the worse it is for the environment. In factory farming, steroids, antibiotics, and growth hormone allow for animals to be raised quickly and thus consume " +
            "less food over time and produce less methane and other waste product. On the other hand, small scale farms take longer to raise animals and as a result are far less efficient. In this, we can see that either welfare or " +
            "environment needs to be sacrificed for animal agriculture to continue. That being said, both are at considerable cost considering animal agriculture's net impact on animal welfare and our environment regardless of how it is done.";

    private static final String PRIVILEGE =
            "Considering much of media interpretation of veganism is wealthy white men, it is clear why an image of privilege appears when veganism is mentioned. In reality, veganism is very much the opposite of privilege for most people. For a significant portion of the world, " +
            "common vegan food is widely available and cheaper than meat. In fact, in many developing nations, the most available food sources are entirely plant based considering they take less resources to grow and can feed more people.";

    private static final String PRIVILEGE_2 =
            "However, vegans recognize that there are certain situations wherein killing other animals is necessary to survive. This could be in situations of extreme famine or in areas of the world with no available alternatives. By the definition of veganism, these people would in " +
            "fact still be vegan. This is because veganism is simply a philosophy to reduce as much as physically possible the cruelty and exploitation of animals. In other words, veganism is mostly about " +
            "treating other animals as moral beings deserving of care rather than objects that can be used as a means to an end.";

    private static final String HUMANS_FIRST =
            "For many, the rights and welfare of humans is a far more pressing issue than the rights and welfare of non-human animals. While this is a perfectly reasonable thought, some use it to ignore any of the harm done to animals " +
            "by stating humans should come first. Like many other fallacious arguments, this uses a false dichotomy fallacy. There is no reason a person would have to give up volunteering to feed the homeless if they had a tofu stir fry instead of a chicken " +
            "stir fry prior to volunteering. To further this point, a plant-based diet is often cheaper than the meat-centered counterpart which gives people more money to use to help those in need. ";

    private static final String BAD_VEGAN =
            "Even with a small amount of time on the internet, it is hard to avoid hearing about \"bad vegans\" or similarly bad \"bad vegan organizations\" like TheVeganTeacher and PETA. This is a standard red herring argument to take away from " +
            "the real issue at hand. Within any movement, organization, company, or otherwise you can always find someone who engages in inflammatory action. As an example, Malcolm X was often viewed as an extremist during the United States civil rights movement. However, that does " +
            "not mean that he did not have a significantly positive impact on many others nor did his actions showcase the civil rights movement to be a bad thing. ";

    private static final String BAD_VEGAN_2 = "Additionally, the often labeled \"bad organizations\" are generally mislabeled. For example, the blame PETA " +
            "gets for killing dogs is all traced back to the fact they run kill shelters. However, the only reason they do this is because they don't have enough donations to feed the animals rescued from puppy mills, abusive owners, etc so they opt for euthanasia instead of starvation.";

    private static final String ECONOMY =
            "For those that are more economically minded, there is an immediate concern for the jobs of animal farmers when discussions of vegan populations comes up. The first response to this issue is to point out another example of capitalism moving " +
            "the economic demand. When renewable energies started to become cheaper than fossil fuels, we didn't say that we shouldn't transition because the people in coal mines would lose their jobs. Instead, we realized that the people who used to work in coal mines " +
            "could transition into new jobs; especially when proper training programs were given by the government. In that same vein, the vegan movement already has produced programs to help animal farms transition to vegan farming practices. Additionally, a majority of " +
            "modern animal factories are run by massive corporations paying poor wages with poor working conditions. Despite the imagery they try to push, losing these companies is not a detriment to anyone but their shareholders.";

    private static final String PREDATORS =
            "One school of thought that is popular with a carnist mentality is that other predators kill animals so we should be able to as well. However, this logic runs into a few road blocks. First, we have to comprehend the idea of \"obligate carnivores\". These are animals " +
            "that cannot survive without killing for food. This is different to something like a facultative carnivore which do not need to eat other animals to survive. Since an obligate carnivore has no choice in the matter, it is difficult to hold them accountable. Similarly, most " +
            "would not fault someone who defended themselves against an attacker considering it was a defense for their life.";

    private static final String PREDATORS_2 =
            "The second issue with this thought process is the fact we are assigning human morals to an animal that is not a moral agent. This is different to saying an animal deserves moral consideration, of course. To illustrate this, let's use an example of a two year old child. If " +
            "the child were to engage in morally wrong behavior, we'd give the child leniency as they don't know any better. They are simply following what they've seen and are incapable of moral decision making; at least, that is, when compared to an adult. However, it'd be difficult to reason " +
            "that adults should be able to have the ability to harm the child simply because they lack moral decision making skills. In this, we see that both children and animals are both deserving of moral consideration by humans but are not moral agents. Therefore, we should not judge other animals " +
            "as we judge other humans.";

    private static final String OMEGA_3 =
            "In recent times, fat has been under the microscope to determine what exactly makes up healthy fat choices. The biggest take away from the current literature is to limit saturated fats while focusing on mono and polyunsaturated fats with a special focus on a specific fatty acid " +
            "called an \"omega-3\". In popular culture, that has translated to cutting out eggs, milk, and flesh from land animals while focusing on fish consumption. However, this only partially covers the issue at hand. First, we need to understand that omega-3s come in many forms with focus on three: " +
            "alpha-linolenic acid (ALA), eicosapentaeonic acid (EPA) and docosahexaenoic acid (DHA). EPA and DHA are generally found in animal sources while ALA is found in plant sources. If we want to conclude that DHA and EPA are inaccessible for plant-based eaters, we might say that a plant-based diet is unhealthy. " +
            "However, this is not the case.";

    private static final String OMEGA_3_2 =
            "First, we need to note that ALA can be converted into EPA and DHA inside the human body. The conversion rate varies but can be estimated at around ~15%. This means, to get the necessary ~100mg of EPA and DHA a day, you need roughly 675mg of ALA. For reference, 3 tbsp of flax meal is ~705mg of ALA which is " +
            "plenty for the recommended daily value. This isn't considering that there are many other sources of ALA throughout the day such as walnuts, chia seeds, some vegetable oils, soy products, and much more. All of this is to say that it is relatively easy to get enough omega-3s from plant based whole foods alone.";

    private static final String OMEGA_3_3 =
            "Finally, it is of value to know the EPA and DHA in fish is not actually synthesized by fish but rather the micro algae they consume. Which is why algae as well as the increasingly popular algae oil supplements (replacement for fish oil) are common for those looking to increase omega-3 levels without " +
            "the hassle of eating whole foods. This is especially important because studies have found that the bioavailability of DHA from algae oil is equivalent to that from cooked salmon.";

    static {
        arguments.put("PLANT_FEELINGS", Arrays.asList(PLANT_FEELINGS, PLANT_FEELINGS_2));
        pictures.put("PLANT_FEELINGS", "https://cdn.discordapp.com/attachments/942575408312881164/971256542609424444/6C7B0777-121D-4A48-AF2E-C1A0F7360F2D_1_105_c.jpeg");
        arguments.put("NIRVANA", Arrays.asList(NIRVANA, NIRVANA_2));
        arguments.put("FOOD_CHAIN", Arrays.asList(FOOD_CHAIN, FOOD_CHAIN_2));
        arguments.put("PRICE", Collections.singletonList(PRICE));
        arguments.put("PESTICIDES", Collections.singletonList(PESTICIDES));
        arguments.put("PROTEIN", Arrays.asList(PROTEIN, PROTEIN_2));
        pictures.put("PROTEIN", "https://cheatdaydesign.com/wp-content/uploads/2019/01/Vegan-Protein-Sources-720x720.jpg.webp");
        arguments.put("TASTE", Arrays.asList(TASTE, TASTE_2));
        pictures.put("TASTE", "https://pbs.twimg.com/media/Bn7Fb4ICEAErPsK?format=png");
        arguments.put("ANCESTORS", Collections.singletonList(ANCESTORS));
        arguments.put("OVERPOPULATION", Arrays.asList(OVERPOPULATION, OVERPOPULATION_2, OVERPOPULATION_3));
        arguments.put("ESTROGEN", Arrays.asList(ESTROGEN, ESTROGEN_2));
        arguments.put("CULTURE", Collections.singletonList(CULTURE));
        arguments.put("HUNTING", Arrays.asList(HUNTING, HUNTING_2, HUNTING_3));
        arguments.put("HUMANE", Arrays.asList(HUMANE_MEAT, HUMANE_MEAT_2, HUMANE_MEAT_3, HUMANE_MEAT_4));
        pictures.put("HUMANE", "https://i0.wp.com/www.eatplantslivewell.com/wp-content/uploads/2015/06/B9rhq9hCAAEjW2Z.jpg");
        arguments.put("PRIVILEGE", Arrays.asList(PRIVILEGE, PRIVILEGE_2));
        arguments.put("HUMANS_FIRST", Collections.singletonList(HUMANS_FIRST));
        arguments.put("BAD_VEGAN", Arrays.asList(BAD_VEGAN, BAD_VEGAN_2));
        arguments.put("ECONOMY", Collections.singletonList(ECONOMY));
        arguments.put("PREDATORS", Arrays.asList(PREDATORS, PREDATORS_2));
        arguments.put("OMEGA_3", Arrays.asList(OMEGA_3, OMEGA_3_2, OMEGA_3_3));

    }
}
