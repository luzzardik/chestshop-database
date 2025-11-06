package io.github.md5sha256.chestshopdatabase.util;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.event.ClickCallback;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class DialogUtil {

    public static final ClickCallback.Options DEFAULT_CALLBACK_OPTIONS =
            ClickCallback.Options.builder().build();

    public static final DialogAction CLOSE_DIALOG_ACTION =
            DialogAction.customClick(DialogUtil::closeDialog, DEFAULT_CALLBACK_OPTIONS);

    public static void closeDialog(@Nonnull DialogResponseView view, @Nonnull Audience audience) {
        audience.closeDialog();
    }

    public static DialogAction openDialogAction(@Nonnull Supplier<Dialog> dialogSupplier) {
        return DialogAction.customClick((view, audience) ->
                        audience.showDialog(dialogSupplier.get())
                , DEFAULT_CALLBACK_OPTIONS);
    }

}
