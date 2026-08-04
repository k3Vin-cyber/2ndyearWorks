import qrcode
import itertools

def main():
    # ─────────────────────────────────────────────────────────────────────────
    # PART 1 — QR Code Generator
    # ─────────────────────────────────────────────────────────────────────────
    print("--- PART 1: QR Code Generator ---")
    
    # STUDENT ACTION: Update this string with your actual name and section!
    student_info = "Dela Cruz, Juan — BSIT 3A"

    # Configure the QR code parameters
    qr = qrcode.QRCode(
        version=1,
        error_correction=qrcode.constants.ERROR_CORRECT_H,
        box_size=12,
        border=4,
    )
    qr.add_data(student_info)
    qr.make(fit=True)

    # Generate and save the image
    img = qr.make_image(fill_color="black", back_color="white")
    img.save("my_qr.png")
    
    print(f"✅ QR Code generated and saved as 'my_qr.png' for: {student_info}\n")


    # ─────────────────────────────────────────────────────────────────────────
    # PART 2 — itertools Permutations
    # ─────────────────────────────────────────────────────────────────────────
    print("--- PART 2: itertools Permutations ---")
    
    items = ["Touch", "Sound", "Vision", "Motion"]
    
    # Generate all permutations and convert to a list to count and slice them
    all_permutations = list(itertools.permutations(items))
    
    print(f"Total possible permutations: {len(all_permutations)}")
    print("First 5 permutations:")
    
    # Print only the first 5
    for i, perm in enumerate(all_permutations[:5], start=1):
        print(f"  {i}. {perm}")
    print()


    # ─────────────────────────────────────────────────────────────────────────
    # PART 3 — Itertools Chunker
    # ─────────────────────────────────────────────────────────────────────────
    print("--- PART 3: itertools Chunker ---")
    
    # Standard itertools 'grouper' recipe
    def grouper(iterable, n, fillvalue=None):
        args = [iter(iterable)] * n
        return itertools.zip_longest(*args, fillvalue=fillvalue)

    modules = list(range(1, 8))
    print(f"Original modules: {modules}")
    print("Chunked by 3:")
    
    for chunk in grouper(modules, 3):
        print(f"  {chunk}")


if __name__ == "__main__":
    main()