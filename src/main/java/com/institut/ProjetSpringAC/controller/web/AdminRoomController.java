package com.institut.ProjetSpringAC.controller.web;

import com.institut.ProjetSpringAC.entity.Room;
import com.institut.ProjetSpringAC.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/rooms")
public class AdminRoomController {

    private final RoomService roomService;

    @Autowired
    public AdminRoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin/rooms/list";
    }

    @GetMapping("/new")
    public String createRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "admin/rooms/form";
    }

    @PostMapping
    public String saveRoom(@ModelAttribute("room") Room room, RedirectAttributes redirectAttributes) {
        try {
            roomService.saveRoom(room);
            redirectAttributes.addFlashAttribute("successMessage", "Salle enregistrée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
        }
        return "redirect:/admin/rooms";
    }

    @GetMapping("/edit/{id}")
    public String editRoomForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return roomService.getRoomById(id)
                .map(room -> {
                    model.addAttribute("room", room);
                    return "admin/rooms/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Salle non trouvée.");
                    return "redirect:/admin/rooms";
                });
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            roomService.deleteRoom(id);
            redirectAttributes.addFlashAttribute("successMessage", "Salle supprimée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/rooms";
    }
}
