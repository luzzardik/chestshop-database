package io.github.md5sha256.chestshopdatabase.gui.dialog;

import io.github.md5sha256.chestshopdatabase.gui.FindState;
import io.github.md5sha256.chestshopdatabase.model.ShopType;
import io.github.md5sha256.chestshopdatabase.util.DialogUtil;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class FilterDialog {

    @Nonnull
    private static DialogBase createShopFiltersBase(@Nonnull Set<ShopType> includedTypes) {
        List<? extends DialogInput> inputs = Arrays.stream(ShopType.values())
                .map(type -> DialogInput.bool(type.name(), Component.text(type.displayName()))
                        .initial(includedTypes.contains(type))
                        .build())
                .toList();

        return DialogBase.builder(Component.text("Select Shop Types"))
                .canCloseWithEscape(true)
                .inputs(inputs)
                .build();
    }

    @Nonnull
    public static Dialog createFiltersDialog(@Nonnull FindState state,
                                             @Nonnull Supplier<Dialog> prevDialog) {
        Set<ShopType> includedTypes = state.shopTypes();
        ActionButton saveButton = ActionButton.builder(Component.text("Save"))
                .tooltip(Component.text("Save selection and return to previous menu"))
                .action(DialogAction.customClick(applyFilters(state, prevDialog),
                        DialogUtil.DEFAULT_CALLBACK_OPTIONS))
                .build();
        ActionButton backButton = ActionButton.builder(Component.text("Back"))
                .tooltip(Component.text("Return to previous menu"))
                .action(DialogUtil.openDialogAction(prevDialog))
                .build();
        return Dialog.create(factory ->
                factory.empty()
                        .base(createShopFiltersBase(includedTypes))
                        .type(DialogType.confirmation(saveButton, backButton))
        );
    }

    private static DialogActionCallback applyFilters(@Nonnull FindState findState,
                                                     @Nonnull Supplier<Dialog> prevDialog) {
        return (view, audience) -> {
            Set<ShopType> included = EnumSet.noneOf(ShopType.class);
            for (ShopType shopType : ShopType.values()) {
                Boolean value = view.getBoolean(shopType.name());
                if (value != null && value) {
                    included.add(shopType);
                }
            }
            findState.setShopTypes(included);
            audience.showDialog(prevDialog.get());
        };
    }

}
